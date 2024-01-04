package io.github.eternalstone.example.controller;

import io.github.eternalstone.example.entity.User;
import io.github.eternalstone.provider.SensitiveWordProvider;
import io.github.eternalstone.tools.SensitiveFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * to do something
 * @author  Justzone on 2022/10/9 16:54
 */
@RestController
public class TestController {

    private Logger logger =
            LoggerFactory.getLogger(TestController.class);

    @Resource
    private SensitiveWordProvider sensitiveWordProvider;

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Integer id){
        User user = new User();
        user.setId(id);
        user.setUsername("张三");
        user.setPassword("123456");
        user.setMobile("13988881111");
        user.setEmail("123@qq.com");
        user.setAddress("浙江省杭州市西湖区xxx街道xxx社区xxx");
        return user;
    }

    @GetMapping("/log/test")
    public String getUser(){
        logger.info("mobile=131251011111");
        logger.info("idcard=422202111111111111");
        logger.info("身份证=422202111111111111,这里的风景美如画,姓名=王五六,mobile=131251011111,身份证=422202111111111122");
        logger.info("微信=asdfsdsdf123sf");
        return "sccuess";
    }

    @GetMapping("/sensitive")
    public List<String> sensitive(String msg){
        return sensitiveWordProvider.contain(msg);
    }


//    public static void main(String[] args) throws IOException {
//        String source = "E:\\cloud_workspace\\SensitiveBye\\sensitivebye-example\\src\\main\\resources\\application.yml";
//        String string = SensitiveFileUtil.sensitiveByeToString(source);
//        System.out.println(string);
//        SensitiveFileUtil.sensitiveByeToFile(source, source);
//    }

}
