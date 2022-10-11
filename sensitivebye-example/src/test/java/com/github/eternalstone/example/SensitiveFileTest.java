package com.github.eternalstone.example;

import com.github.eternalstone.attachment.file.handle.SensitiveFileHandler;
import com.github.eternalstone.example.sensitvebye.SensitiveCustomeFilterHandler;
import com.github.eternalstone.tools.SensitiveFileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * to do something
 *
 * @author Justzone on 2022/10/9 17:18
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SensitiveByeExampleApplication.class)
public class SensitiveFileTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveFileTest.class);

    @Test
    public void test() throws IOException {
        //输入配置文件绝对路径，仅支持SpringBoot配置文件，properties, yaml, yml
        String source = "D:\\document_workspace\\SensitiveBye\\sensitivebye-example\\src\\main\\resources\\application.yml";
        String s = SensitiveFileUtil.sensitiveByeToString(source);
        LOGGER.info("配置文件脱敏后-------------");
        LOGGER.info("{}", s);
        //可将脱敏后的配置输出到另一个文件
        String target = "D:\\document_workspace\\SensitiveBye\\sensitivebye-example\\src\\main\\resources\\application2.yml";
        SensitiveFileUtil.sensitiveByeToFile(source, target);
        //添加配置文件脱敏关键词
        SensitiveFileHandler handler = new SensitiveFileHandler();
        handler.setNextHandler(new SensitiveCustomeFilterHandler());
        String s2 = SensitiveFileUtil.sensitiveByeToString(source, handler);
        LOGGER.info("配置文件脱敏后-------------");
        LOGGER.info("{}", s2);
    }


}
