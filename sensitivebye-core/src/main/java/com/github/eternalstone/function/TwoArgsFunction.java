package com.github.eternalstone.function;

/**
 * to do something
 *
 * @author Justzone on 2022/8/31 13:44
 */
@FunctionalInterface
public interface TwoArgsFunction<T, S, R> {

    R apply(T var1, S var2);

}
