package io.github.eternalstone.provider.strategy;

import io.github.eternalstone.enums.SensitiveType;
import io.github.eternalstone.function.TwoArgsFunction;

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
