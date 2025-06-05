package io.github.eternalstone.configuration;

import io.github.eternalstone.aop.MybatisSensitiveInterceptor;
import io.github.eternalstone.properties.GlobalSensitiveByeProperties;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置mybatis脱敏开关，开则注入mybatis脱敏组件
 *
 * @author Justzone on 2022/9/28 11:27
 */
@ConditionalOnProperty(name = GlobalSensitiveByeProperties.PREFIX + ".mybatis.enabled", havingValue = "true")
@Configuration
public class MybatisSensitiveByeConfiguration {

    @Bean
    @ConditionalOnMissingBean(ConfigurationCustomizer.class)
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> configuration.addInterceptor(new MybatisSensitiveInterceptor());
    }


}
