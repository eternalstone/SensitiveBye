package com.github.eternalstone.example.sensitvebye;

import com.github.eternalstone.attachment.algorithm.ICipherAlgorithm;

/**
 * to do something
 *
 * @author Justzone on 2022/10/10 10:49
 */
public class MobileAlgorithm implements ICipherAlgorithm {

    @Override
    public String encrypt(String value) {
        return "*" + value +"*";
    }

    @Override
    public String decrypt(String value) {
        return value.replaceAll("\\*", "");
    }
}
