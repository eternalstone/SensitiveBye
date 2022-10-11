package com.github.eternalstone.provider.strategy;

import com.github.eternalstone.function.TwoArgsFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
/**
 * to do something
 *
 * @author Justzone on 2022/8/31 11:42
 */
public abstract class AbstractFieldStrategy implements IFieldStrategy {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Map<String, TwoArgsFunction<String, String, String>> strategyGroup = new HashMap<>(8);

    @Override
    public IFieldStrategy add(String key, TwoArgsFunction<String, String, String> func) {
        strategyGroup.put(key, func);
        return this;
    }

    @Override
    public String handle(String key, String value, String symbol) {
        if (strategyGroup.containsKey(key)) {
            return strategyGroup.get(key).apply(value, symbol);
        } else {
            logger.debug("key `{}` not defined in custome strategy", key);
            return value;
        }
    }
}
