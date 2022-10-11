package io.github.eternalstone.configuration;

import io.github.eternalstone.attachment.log.ISensitiveLogRule;
import io.github.eternalstone.properties.GlobalSensitiveByeProperties;
import io.github.eternalstone.provider.SensitiveFieldProvider;
import io.github.eternalstone.provider.SensitiveLogProvider;
import io.github.eternalstone.provider.strategy.CustomeFieldStrategy;
import io.github.eternalstone.util.SpringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 全局自动配置类
 *
 * @author Justzone on 2022/9/4 14:30
 */
@EnableConfigurationProperties(GlobalSensitiveByeProperties.class)
@Configuration
public class GlobalSensitiveByeAutoConfiguration {


    /**
     * SpringUtil工具
     */
    @Bean
    @ConditionalOnMissingBean(SpringUtil.class)
    public SpringUtil springUtil() {
        return new SpringUtil();
    }

    /**
     * 注入默认的字段脱敏器
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = GlobalSensitiveByeProperties.PREFIX + ".field.enabled", havingValue = "true", matchIfMissing = true)
    public SensitiveFieldProvider sensitiveFieldProvider() {
        SensitiveFieldProvider sensitiveFieldProvider = SensitiveFieldProvider.instance();
        CustomeFieldStrategy strategy = SpringUtil.getBean(CustomeFieldStrategy.class);
        sensitiveFieldProvider.setCustomeStrategy(strategy);
        return sensitiveFieldProvider;
    }

    /**
     * 配置log脱敏开关，开则注入log脱敏组件
     */
    @Bean
    @ConditionalOnProperty(value = GlobalSensitiveByeProperties.PREFIX + ".log.enabled", havingValue = "true")
    public SensitiveLogProvider sensitiveLogProvider() {
        SensitiveLogProvider sensitiveLogProvider = SensitiveLogProvider.instance();
        sensitiveLogProvider.setSensitiveRule(SpringUtil.getBean(ISensitiveLogRule.class));
        return sensitiveLogProvider;
    }

}
