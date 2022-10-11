package com.github.eternalstone.provider.strategy;

import com.github.eternalstone.enums.SensitiveType;
import com.github.eternalstone.function.TwoArgsFunction;

/**
 * to do something
 *
 * @author Justzone on 2022/8/31 10:25
 */
public class DefaultFieldStrategy extends AbstractFieldStrategy {

    public DefaultFieldStrategy add(SensitiveType type, TwoArgsFunction<String, String, String> func) {
        return (DefaultFieldStrategy) super.add(type.name(), func);
    }
}
