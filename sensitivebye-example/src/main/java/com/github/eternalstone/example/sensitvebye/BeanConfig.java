package com.github.eternalstone.example.sensitvebye;

import com.github.eternalstone.attachment.wordsource.SensitiveWordSourceFromResource;
import com.github.eternalstone.provider.SensitiveWordProvider;
import com.github.eternalstone.provider.strategy.CustomeFieldStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * to do something
 *
 * @author Justzone on 2022/10/10 9:20
 */
@Configuration
public class BeanConfig {

    @Bean
    public CustomeFieldStrategy customeFieldStrategy(){
        CustomeFieldStrategy strategy = new CustomeFieldStrategy();
        strategy.add("test", (var1, var2)-> var1.concat(var2));
        return strategy;
    }

    @Bean
    public SensitiveWordProvider sensitiveWordProvider(){
        return new SensitiveWordProvider(new SensitiveWordSourceFromResource());
    }

}
