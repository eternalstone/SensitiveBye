package com.github.eternalstone.provider;

import com.github.eternalstone.enums.SensitiveType;
import com.github.eternalstone.provider.strategy.CustomeFieldStrategy;
import com.github.eternalstone.provider.strategy.IFieldStrategy;
import com.github.eternalstone.provider.model.SensitiveFieldWrapper;
import com.github.eternalstone.serializer.SensitiveSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字段脱敏
 *
 * @author Justzone on 2022/8/30 14:56
 */
public class SensitiveFieldProvider implements ISensitiveFieldProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveFieldProvider.class);
    private static volatile SensitiveFieldProvider INSTANCE;

    private IFieldStrategy defaultStrategy = getDefaultStrategy();
    private IFieldStrategy customeStrategy;

    private SensitiveFieldProvider() {
        SensitiveSerializer.setSensitiveFieldProvider(this);
    }

    public static SensitiveFieldProvider instance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (SensitiveFieldProvider.class) {
            if (INSTANCE != null) {
                return INSTANCE;
            }
            INSTANCE = new SensitiveFieldProvider();
            return INSTANCE;
        }
    }

    @Override
    public String handle(String value, SensitiveFieldWrapper wrapper) {
        if (wrapper.getStrategy() == SensitiveType.CUSTOME) {
            return handle(wrapper.getKey(), value, wrapper.getSymbol());
        }
        return handle(wrapper.getStrategy(), value, wrapper.getSymbol());
    }

    /**
     * 默认策略脱敏
     *
     * @param strategy
     * @param value
     * @param symbol
     * @return
     */
    public String handle(SensitiveType strategy, String value, String symbol) {
        return defaultStrategy.handle(strategy.name(), value, symbol);
    }

    /**
     * 自定义策略脱敏
     *
     * @param key
     * @param value
     * @param symbol
     * @return
     */
    public String handle(String key, String value, String symbol) {
        if (customeStrategy == null) {
            LOGGER.warn("You have not set a custom desensitization strategy named `{}`", key);
            return value;
        }
        return customeStrategy.handle(key, value, symbol);
    }

    /**
     * 设置自定义脱敏策略
     *
     * @param customeFieldStrategy
     */
    public void setCustomeStrategy(CustomeFieldStrategy customeFieldStrategy) {
        this.customeStrategy = customeFieldStrategy;
    }

}
