package io.github.eternalstone.example;

import io.github.eternalstone.example.entity.User;
import io.github.eternalstone.example.mapper.UserMapper;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * SpringBoot项目需要开启 sensitive-bye.mybatis.enabled=true
 * 其他java项目需要将 MybatisSensitiveInterceptor装载到SpringBean中
 * 注：如果项目中有多个Mybatis拦截器， 注意拦截器的执行顺序
 * 如何使用：
 * 在需要进行加解密的mapper方法上注解@EnableCipher, 该注解有两个参数 parameter和result, 选择对入参和返回参数进行加密还是解密
 * 在实体类上注解CipherField传入加解密算法 ICipherAlgorithm接口的实现类
 * 框架对入参只做mybatis层的数据加解密处理，最后在用户业务层依然是处理前的值，对业务无入侵影响
 *
 * @author Justzone on 2022/10/9 17:18
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SensitiveByeExampleApplication.class)
public class SensitiveMybatisTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveMybatisTest.class);

    @Resource
    private UserMapper userMapper;

    @Test
    public void testInsertAndReturnId() {
        User user = new User();
        user.setUsername("张三");
        user.setPassword("123456");
        user.setMobile("13988881111");
        user.setEmail("123@qq.com");
        user.setAddress("浙江省杭州市西湖区xxx街道xxx社区xxx");
        userMapper.insertAndReturnId(user);
        //输出日志时可关闭日志脱敏避免日志打印带来的影响
        LOGGER.info("数据参数:{}", user.toString());
        //此时数据库中的password已经编码成了'123456******'，但user对象中的password依然是'123456'
    }

    @Test
    public void testInsert(){
        User user = new User();
        user.setUsername("张三");
        user.setPassword("123456");
        user.setMobile("13988881111");
        user.setEmail("123@qq.com");
        user.setAddress("浙江省杭州市西湖区xxx街道xxx社区xxx");
        userMapper.insert(user);
        LOGGER.info("数据参数:{}", user.toString());
    }

    @Test
    public void testInsertBatch(){
        List<User> list = new ArrayList<>(2);
        User user1 = new User();
        user1.setUsername("张三");
        user1.setPassword("123456");
        user1.setMobile("13988881111");
        user1.setEmail("123@qq.com");
        user1.setAddress("浙江省杭州市西湖区xxx街道xxx社区xxx");
        list.add(user1);
        User user2 = new User();
        user2.setUsername("张三");
        user2.setPassword("5678");
        user2.setMobile("13911112222");
        user2.setEmail("456@qq.com");
        user2.setAddress("浙江省杭州市上城区xxx街道xxx社区xxx");
        list.add(user2);
        userMapper.insertBatch(list);
        list.forEach(user -> LOGGER.info("数据参数:{}", user.toString()));
    }

    @Test
    public void testUpdate(){
        User user = new User();
        user.setId(1);
        user.setUsername("李四");
        user.setPassword("8888");
        user.setMobile("13100000000");
        user.setEmail("aaa@qq.com");
        user.setAddress("浙江省杭州市西湖区");
        userMapper.updateById(user);
        LOGGER.info("数据参数:{}", user.toString());
    }

    @Test
    public void testSelectById(){
        User user = userMapper.selectById(1);
        LOGGER.info("返回数据:{}", user.toString());
    }

    @Test
    public void testSelectList(){
        List<User> users = userMapper.selectList(Lists.newArrayList(6, 7, 8, 9));
        users.forEach(user -> LOGGER.info("返回数据:{}", user.toString()));
    }

    @Test
    public void testSelectOne(){
        //可以同时使用参数加密和结果解密，适用于传递明文参数查询数据密文相对应的结果，并且返回结果明文
        //例如 数据库已经存在mobile为密文值为 '*13911112222*', 通过明文'13911112222'查询出user对象中mobile也为明文
        User param = new User();
        param.setMobile("13911112222");
        User user = userMapper.selectByUser(param);
        LOGGER.info("返回数据:{}", user.toString());
    }

    @Test
    @Transactional
    public void testSelectUseCache(){
        //测试在同一个session中的两次一样的查询，加解密是否会走mybatis的一级缓存
        User user1 = userMapper.selectById(1);
        LOGGER.info("返回数据:{}", user1.toString());
        User user2 = userMapper.selectById(1);
        LOGGER.info("返回数据:{}", user2.toString());
    }

}
