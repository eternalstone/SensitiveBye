package com.github.eternalstone.aop;

import com.github.eternalstone.annotation.CipherField;
import com.github.eternalstone.annotation.EnableCipher;
import com.github.eternalstone.attachment.algorithm.ICipherAlgorithm;
import com.github.eternalstone.enums.CipherType;
import com.github.eternalstone.exception.SensitiveByeException;
import com.github.eternalstone.tools.ReflectionUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.executor.BaseExecutor;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 基于mybatis拦截器实现查询字段脱敏，敏感字段加解密
 *
 * @author Justzone on 2022/9/4 15:07
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
public class MybatisSensitiveInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisSensitiveInterceptor.class);
    private ThreadLocal<Map<Object, Map<Field, CipherValue>>> cipherFieldMapLocal = ThreadLocal
            .withInitial(ConcurrentHashMap::new);
    private static final String SELECT_KEY = "!selectKey";
    private static final int EXECUTOR_PARAMETER_COUNT_4 = 4;
    private static final int MAPPED_STATEMENT_INDEX = 0;
    private static final int PARAMETER_INDEX = 1;
    private static final int ROW_BOUNDS_INDEX = 2;
    private static final int CACHE_KEY_INDEX = 4;

    private final Map<String, ICipherAlgorithm> algorithmMap = new ConcurrentHashMap<>(2);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[MAPPED_STATEMENT_INDEX];
        Object parameter = args[PARAMETER_INDEX];
        //处理selectKey的特殊情况
        handleSelectKey(statement, parameter);
        EnableCipher enableCipher = getEnableCipher(statement);
        if (enableCipher == null) {
            return invocation.proceed();
        }
        //获取所有的注解参数字段，根据每个字段对应的算法进行加密或解密
        handleParameter(parameter, enableCipher.parameter());
        //判断是否命中缓存
        boolean hitCache = hitCache(invocation, parameter);
        //执行proceed
        Object proceed;
        try {
            proceed = invocation.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            //还原参数
            revertParameter(parameter);
        }
        //执行结果处理
        return hitCache ? proceed : handleResult(proceed, enableCipher.result());
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 获取mapper上的EnableCipher注解
     *
     * @param statement MappedStatement对象
     * @return 返回EnableCipher注解对象
     */
    private EnableCipher getEnableCipher(MappedStatement statement) {
        String namespace = statement.getId();
        String className = namespace.substring(0, namespace.lastIndexOf("."));
        String methodName = statement.getId().substring(statement.getId().lastIndexOf(".") + 1);
        Method[] methods;
        try {
            methods = Class.forName(className).getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    if (method.isAnnotationPresent(EnableCipher.class)) {
                        return method.getAnnotation(EnableCipher.class);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            LOGGER.error("get @EnableCipher from {} error!", namespace);
        }
        return null;
    }

    /**
     * 处理selectKey
     *
     * @param statement
     * @param parameter
     */
    private void handleSelectKey(MappedStatement statement, Object parameter) {
        SqlCommandType commandType = statement.getSqlCommandType();
        if (commandType == SqlCommandType.SELECT && statement.getId().endsWith(SELECT_KEY)) {
            revertParameter(parameter);
        }
    }

    /**
     * 处理参数
     *
     * @param parameter  输入参数
     * @param cipherType 加解密方式
     * @return 是否加解密
     */
    private void handleParameter(Object parameter, CipherType cipherType) {
        if (cipherType == CipherType.NONE) {
            return;
        }
        Map<Object, Map<Field, CipherValue>> cipherMap = new HashMap<>();
        if (parameter instanceof Map) {
            Map<String, Object> map = filterRepeatValueMap((Map<String, Object>) parameter);
            map.forEach((k, v) -> {
                Map<Object, Map<Field, CipherValue>> valueMap;
                if (v instanceof Collection) {
                    valueMap = handleCipher((Collection) v, cipherType);
                } else {
                    valueMap = handleCipher(Collections.singleton(v), cipherType);
                }
                if (valueMap != null && !valueMap.isEmpty()) {
                    cipherMap.putAll(valueMap);
                }
            });
        } else {
            Map<Object, Map<Field, CipherValue>> valueMap = handleCipher(Collections.singleton(parameter), cipherType);
            if (valueMap != null && !valueMap.isEmpty()) {
                cipherMap.putAll(valueMap);
            }
        }
        //ThreadLocal临时保存处理过的字段值
        if (!cipherMap.isEmpty()) {
            cipherFieldMapLocal.get().putAll(cipherMap);
            //可以打印参数加密前和加密后的值 cipherMap
        }
    }

    /**
     * 处理结果
     *
     * @param result     执行结果对象
     * @param cipherType 加解密方式
     * @return 返回处理后的结果对象
     */
    private Object handleResult(Object result, CipherType cipherType) {
        if (cipherType == CipherType.NONE) {
            return result;
        }
        if (result instanceof Collection) {
            handleCipher((Collection) result, cipherType);
        } else {
            handleCipher(Collections.singleton(result), cipherType);
        }
        return result;
    }

    /**
     * 还原参数
     *
     * @param parameter
     */
    private void revertParameter(Object parameter) {
        final Map<Object, Map<Field, CipherValue>> cipherFieldMap = cipherFieldMapLocal.get();
        if (cipherFieldMap.isEmpty()) {
            return;
        }
        if (parameter instanceof Map) {
            Map<String, Object> parameterMap = filterRepeatValueMap((Map<String, Object>) parameter);
            parameterMap.forEach((k, v) -> {
                if (v instanceof Collection) {
                    ((Collection) v).stream().filter(Objects::nonNull).forEach(
                            obj -> {
                                Map<Field, CipherValue> valueMap = cipherFieldMap.get(obj);
                                if (Objects.nonNull(valueMap)) {
                                    valueMap.forEach((field, cipher) -> ReflectionUtils.setField(field, obj, cipher.getBefore()));
                                }
                            });
                } else {
                    Map<Field, CipherValue> valueMap = cipherFieldMap.get(v);
                    if (Objects.nonNull(valueMap)) {
                        valueMap.forEach((field, cipher) -> ReflectionUtils.setField(field, v, cipher.getBefore()));
                    }
                }
            });
        } else {
            Map<Field, CipherValue> valueMap = cipherFieldMap.get(parameter);
            if (Objects.nonNull(valueMap)) {
                valueMap.forEach((field, cipher) -> ReflectionUtils.setField(field, parameter, cipher.getBefore()));
            }
        }
        cipherFieldMap.clear();
    }

    /**
     * 查询语句是否命中缓存
     *
     * @param invocation 拦截器方法对象
     * @param parameter  处理过后的参数对象
     * @return 是否命中缓存
     */
    private boolean hitCache(Invocation invocation, Object parameter) throws IllegalAccessException {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[MAPPED_STATEMENT_INDEX];
        Executor executor = (Executor) invocation.getTarget();
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        //非查询语句直接返回false
        if (!SqlCommandType.SELECT.equals(sqlCommandType)) {
            return false;
        }
        RowBounds rowBounds = (RowBounds) args[ROW_BOUNDS_INDEX];
        BoundSql boundSql;
        CacheKey cacheKey;
        if (args.length == EXECUTOR_PARAMETER_COUNT_4) {
            boundSql = mappedStatement.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(mappedStatement, parameter, rowBounds, boundSql);
        } else {
            cacheKey = (CacheKey) args[CACHE_KEY_INDEX];
        }
        Executor baseExecutor;
        if (executor instanceof CachingExecutor) {
            Field field = ReflectionUtils.findField(CachingExecutor.class, "delegate");
            assert field != null;
            field.setAccessible(true);
            baseExecutor = (Executor) field.get(executor);
        } else {
            baseExecutor = (BaseExecutor) invocation.getTarget();
        }
        Field field = ReflectionUtils.findField(BaseExecutor.class, "localCache");
        assert field != null;
        field.setAccessible(true);
        PerpetualCache localCache = (PerpetualCache) field.get(baseExecutor);
        return Objects.nonNull(localCache.getObject(cacheKey));
    }


    /**
     * 获取算法对象
     *
     * @param value 加解密算法子类
     * @return 返回算法对象
     */
    private ICipherAlgorithm getCipherAlgorithm(Class<? extends ICipherAlgorithm> value) {
        String canonicalName = value.getCanonicalName();
        if (algorithmMap.containsKey(canonicalName)) {
            return algorithmMap.get(canonicalName);
        }
        try {
            ICipherAlgorithm algorithm = value.getDeclaredConstructor().newInstance();
            algorithmMap.put(value.getName(), algorithm);
            return algorithm;
        } catch (Exception e) {
            throw new SensitiveByeException("init ICipherAlgorithm error", e);
        }
    }

    /**
     * 加解密操作对象
     *
     * @param collection 输入参数
     * @param cipherType 加解密方式
     * @return 返回已处理字段的处理前后值
     */
    private Map<Object, Map<Field, CipherValue>> handleCipher(Collection collection, CipherType cipherType) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        //遍历参数，处理加解密
        Map<Object, Map<Field, CipherValue>> result = new HashMap<>();
        collection.forEach(object -> {
            Map<Field, CipherValue> valueMap = new HashMap<>();
            this.getFields(object).stream()
                    .filter(field -> field.isAnnotationPresent(CipherField.class) && field.getType() == String.class)
                    .forEach(field -> {
                        CipherField cipherField = field.getAnnotation(CipherField.class);
                        ICipherAlgorithm algorithm = getCipherAlgorithm(cipherField.value());
                        String value = (String) getField(field, object);
                        if (Objects.nonNull(value)) {
                            String algorithmValue = null;
                            if (cipherType == CipherType.ENCRYPT) {
                                algorithmValue = algorithm.encrypt(value);
                            }
                            if (cipherType == CipherType.DECRYPT) {
                                algorithmValue = algorithm.decrypt(value);
                            }
                            if (Objects.nonNull(algorithmValue)) {
                                ReflectionUtils.setField(field, object, algorithmValue);
                                valueMap.put(field, new CipherValue(value, algorithmValue));
                            }
                        }
                    });
            if (!valueMap.isEmpty()) {
                result.put(object, valueMap);
            }
        });
        return result;
    }

    private Map<String, Object> filterRepeatValueMap(Map<String, Object> parameter) {
        Set<Integer> hashCodeSet = new HashSet<>();
        return (parameter).entrySet().stream().filter(e -> Objects.nonNull(e.getValue())).filter(r -> {
            if (!hashCodeSet.contains(r.getValue().hashCode())) {
                hashCodeSet.add(r.getValue().hashCode());
                return true;
            }
            return false;
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<Field> getFields(Object obj) {
        List<Field> fieldList = new ArrayList<>();
        Class tempClass = obj.getClass();
        while (tempClass != null) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }
        return fieldList;
    }

    private Object getField(Field field, Object obj) {
        ReflectionUtils.makeAccessible(field);
        return ReflectionUtils.getField(field, obj);
    }

    static class CipherValue {
        String before;
        String after;

        public CipherValue(String before, String after) {
            this.before = before;
            this.after = after;
        }

        public String getBefore() {
            return before;
        }

        public String getAfter() {
            return after;
        }
    }
}
