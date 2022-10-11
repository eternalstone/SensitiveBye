package com.github.eternalstone.example;

import com.alibaba.fastjson.JSONObject;
import com.github.eternalstone.provider.SensitiveWordProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


/**
 * to do something
 *
 * @author Justzone on 2022/10/9 20:27
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SensitiveByeExampleApplication.class)
public class SensitiveWordTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveWordTest.class);

    @Bean
    public SensitiveWordProvider sensitiveWordProvider(){
        /**
         * 无参构造默认读取resource下的sensitive.txt敏感词库
         * 内置 SensitiveWordSourceFromUrl对象传入url可从网络地址中读取词库文件
         * 自定义读取词库，可实现 ISensitiveWordSource接口的loadSource()方法
         * 词库一旦初始化会暂存在内存中，如需动态更新词库，调用 sensitiveWordProvider.reload()即可重新加载词库
         */
        return new SensitiveWordProvider();
    }

    @Test
    public void test(){
        String str = "俄罗斯攻打乌克兰";
        List<String> words = sensitiveWordProvider().contain(str);
        LOGGER.info("包含敏感词：{}", JSONObject.toJSONString(words));
        LOGGER.info("替换敏感词：{}", sensitiveWordProvider().handle(str, "*"));
        LOGGER.info("--------------------");
        String str2 = "中国是一个发展中国家";
        List<String> words2 = sensitiveWordProvider().contain(str2);
        LOGGER.info("包含敏感词：{}", JSONObject.toJSONString(words2));
        LOGGER.info("替换敏感词：{}", sensitiveWordProvider().handle(str2, "*"));

    }


}
