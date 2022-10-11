package com.github.eternalstone.filter;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.github.eternalstone.annotation.SensitiveBye;
import com.github.eternalstone.provider.ISensitiveFieldProvider;
import com.github.eternalstone.provider.model.SensitiveFieldWrapper;
import com.github.eternalstone.serializer.SensitiveSerializer;
import com.github.eternalstone.tools.SensitiveFieldUtil;

import java.io.SyncFailedException;
import java.lang.reflect.Field;

/**
 * fastjson对sensitivebye注解的支持
 *
 * @author Justzone on 2022/8/30 14:37
 */
public class SensitiveByeFilter implements ValueFilter {

    private static volatile SensitiveByeFilter INSTANCE;

    private SensitiveByeFilter() {

    }

    @Override
    public Object process(Object obj, String name, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            if (field.getType() != String.class) {
                return value;
            }
            SensitiveBye annotation = field.getAnnotation(SensitiveBye.class);
            if (annotation == null) {
                return value;
            }
            ISensitiveFieldProvider provider = SensitiveSerializer.getSensitiveFieldProvider();
            if (provider == null) {
                throw new SyncFailedException("You used the annotation `@SensitiveBye` but did not inject `SensitiveFieldProvider`");
            } else {
                if (SensitiveFieldUtil.isSkipSensitive(name)) {
                    return value;
                } else {
                    return provider.handle(value.toString(), new SensitiveFieldWrapper(annotation, name));
                }
            }
        } catch (NoSuchFieldException e) {
            return value;
        } catch (Exception e) {
            return value;
        }
    }

    public static SensitiveByeFilter instance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (SensitiveByeFilter.class) {
            if (INSTANCE != null) {
                return INSTANCE;
            }
            INSTANCE = new SensitiveByeFilter();
            return INSTANCE;
        }
    }
}
