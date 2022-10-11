package com.github.eternalstone.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.regex.Pattern;


/**
 * to do something
 *
 * @author Justzone on 2022/10/9 20:27
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SensitiveByeExampleApplication.class)
public class SensitiveLogTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveLogTest.class);

    @Test
    public void test() {
        /**
         * 开启日志脱敏配置开关
         * logback需要配置converter com.github.eternalstone.attachment.log.converter.LogbackSensitiveConverter
         * log4j2需要替换%msg为%sdmsg
         * 如需自定义日志的脱敏规则，实现ISensitiveLogRule接口的custome方法即可
         */
        LOGGER.info("姓名:{}", "张三");
        LOGGER.info("mobile:{}", "13188889999");
    }


}
