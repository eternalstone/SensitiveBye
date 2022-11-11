package io.github.eternalstone.example.controller;

import io.github.eternalstone.example.entity.User;
import io.github.eternalstone.provider.SensitiveWordProvider;
import io.github.eternalstone.tools.SensitiveFileUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * to do something
 * @author  Justzone on 2022/10/9 16:54
 */
@RestController
public class TestController {

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

    @GetMapping("/sensitive")
    public List<String> sensitive(String msg){
        return sensitiveWordProvider.contain(msg);
    }


    public static void main(String[] args) throws IOException {
        String source = "E:\\cloud_workspace\\SensitiveBye\\sensitivebye-example\\src\\main\\resources\\application.yml";
        String string = SensitiveFileUtil.sensitiveByeToString(source);
        System.out.println(string);
        SensitiveFileUtil.sensitiveByeToFile(source, source);
    }

}
