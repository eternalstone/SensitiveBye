package io.github.eternalstone.annotation;

import io.github.eternalstone.enums.CipherType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库编码开关
 *
 * @author Justzone on 2022/9/22 16:44
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface EnableCipher {

    /**
     * 参数处理形式：可加密，可解密，默认不处理
     * @return
     */
    CipherType parameter() default CipherType.NONE;

    /**
     * 结果处理形式：可加密，可解密，默认不处理
     * @return
     */
    CipherType result() default CipherType.NONE;

}
