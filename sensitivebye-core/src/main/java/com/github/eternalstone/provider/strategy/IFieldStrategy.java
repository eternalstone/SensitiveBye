package com.github.eternalstone.provider.strategy;

import com.github.eternalstone.function.TwoArgsFunction;
/**
 * to do something
 *
 * @author Justzone on 2022/8/31 10:25
 */
public interface IFieldStrategy {

    IFieldStrategy add(String key, TwoArgsFunction<String, String, String> func);

    String handle(String key, String value, String symbol);

}
