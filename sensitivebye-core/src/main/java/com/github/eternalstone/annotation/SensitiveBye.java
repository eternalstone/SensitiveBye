package com.github.eternalstone.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.eternalstone.enums.SensitiveType;
import com.github.eternalstone.serializer.SensitiveSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段脱敏注解
 *
 * @author Justzone on 2022/8/29 11:42
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveSerializer.class)
public @interface SensitiveBye {

    /**
     * 自定义脱敏值
     * @return
     */
    String value() default "";

    /**
     * 脱敏策略
     * @return
     */
    SensitiveType strategy() default SensitiveType.CUSTOME;


    /**
     * 脱敏替换符号
     * @return
     */
    String symbol() default "*";

}
