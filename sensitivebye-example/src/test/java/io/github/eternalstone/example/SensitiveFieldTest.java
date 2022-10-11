package io.github.eternalstone.example;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.eternalstone.enums.SensitiveType;
import io.github.eternalstone.example.entity.User;
import io.github.eternalstone.filter.SensitiveByeFilter;
import io.github.eternalstone.provider.SensitiveFieldProvider;
import io.github.eternalstone.provider.strategy.CustomeFieldStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

/**
 * to do something
 *
 * @author Justzone on 2022/10/9 17:18
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SensitiveByeExampleApplication.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class SensitiveFieldTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveFieldTest.class);

    @Resource
    private WebApplicationContext webApplicationContext;

    @Resource
    private MockMvc mockMvc;

    @Before()
    public void setup () {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    /**
     * java对象字段脱敏
     */
    @Test
    public void test1() {
        User user = new User();
        user.setMobile("13988881111");
        LOGGER.info("脱敏前:{}", user.getMobile());
        user.setMobile(SensitiveFieldProvider.instance().handle(SensitiveType.MOBILE, user.getMobile(), "*"));
        LOGGER.info("脱敏后:{}", user.getMobile());
        LOGGER.info("---------自定义脱敏策略----------");
        //添加自定义字段脱敏策略
        CustomeFieldStrategy strategy = new CustomeFieldStrategy();
        strategy.add("test", (var1, var2)-> var1.concat(var2));
        SensitiveFieldProvider instance = SensitiveFieldProvider.instance();
        instance.setCustomeStrategy(strategy);

        user.setUsername("张三");
        LOGGER.info("脱敏前:{}", user.getUsername());
        user.setUsername(instance.handle("test", user.getUsername(), "*"));
        LOGGER.info("脱敏后:{}", user.getUsername());
    }

    /**
     * json序列化, 在需要脱敏的字段上注解@SensitiveBye
     */
    @Test
    public void test2() throws JsonProcessingException {
        //自定义字段脱敏策略
        CustomeFieldStrategy strategy = new CustomeFieldStrategy();
        strategy.add("test", (var1, var2)-> var1.concat(var2));
        SensitiveFieldProvider instance = SensitiveFieldProvider.instance();
        instance.setCustomeStrategy(strategy);

        User user = new User();
        user.setUsername("张三");
        user.setMobile("13988881111");
        //jackson序列化
        ObjectMapper mapper = new ObjectMapper();
        LOGGER.info("jackson序列化脱敏:{}", mapper.writeValueAsString(user));
        //fastjson序列化, 需要添加一个fastjson的值过滤器，SensitiveBye已经内置实现了SensitiveByeFilter
        LOGGER.info("fastjson序列化脱敏:{}", JSONObject.toJSONString(user, SensitiveByeFilter.instance()));
    }

    /**
     * 接口字段脱敏, 在需要脱敏的字段上注解@SensitiveBye
     */
    @Test
    public void test3() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders.get("/user/1");
        postRequestBuilder.contentType(MediaType.APPLICATION_JSON);
        ResultActions resultActions = mockMvc.perform(postRequestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        MvcResult response = resultActions.andReturn();
        LOGGER.info("接口脱敏响应：{}", new String(response.getResponse().getContentAsByteArray()));
    }


}
