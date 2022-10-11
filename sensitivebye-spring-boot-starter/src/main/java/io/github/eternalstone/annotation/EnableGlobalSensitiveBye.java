package io.github.eternalstone.annotation;

import io.github.eternalstone.configuration.GlobalSensitiveByeAutoConfiguration;
import io.github.eternalstone.configuration.MybatisSensitiveByeConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启全局配置注解
 *
 * @author Justzone on 2022/9/4 14:55
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(GlobalSensitiveByeAutoConfiguration.class)
public @interface EnableGlobalSensitiveBye {

}
