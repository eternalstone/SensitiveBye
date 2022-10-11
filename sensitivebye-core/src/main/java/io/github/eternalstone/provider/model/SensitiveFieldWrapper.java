package io.github.eternalstone.provider.model;

import io.github.eternalstone.annotation.SensitiveBye;
import io.github.eternalstone.enums.SensitiveType;

/**
 * to do something
 *
 * @author Justzone on 2022/8/31 11:24
 */
public class SensitiveFieldWrapper {

    private SensitiveType strategy;

    private String name;

    private String key;

    private String symbol;

    public SensitiveFieldWrapper(SensitiveBye annotation, String name) {
        this.strategy = annotation.strategy();
        this.key = annotation.value();
        this.symbol = annotation.symbol();
        this.name = name;
    }

    public SensitiveType getStrategy() {
        return strategy;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }


    public String getSymbol() {
        return symbol;
    }

}
