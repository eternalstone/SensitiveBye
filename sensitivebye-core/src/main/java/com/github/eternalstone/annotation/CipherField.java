package com.github.eternalstone.annotation;

import com.github.eternalstone.attachment.algorithm.ICipherAlgorithm;

import java.lang.annotation.*;

/**
 * 字段加解密
 *
 * @author Justzone on 2022/8/29 11:42
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CipherField {

    Class<? extends ICipherAlgorithm> value();

}
