# SensitiveBye
![MavenCentral](https://img.shields.io/maven-central/v/io.github.eternalstone/sensitivebye-spring-boot-starter?style=flat-square)
![Hex.pm](https://img.shields.io/hexpm/l/plug.svg?style=flat-square)



## 1、简介

一款专注于解决数据脱敏的Java工具包, 能帮助您快速解决项目中的脱敏需求，支持对接口字段、java对象字段和json序列化字段脱敏；常见日志框架(logback,log4j2)输出内容脱敏；基于mybatis拦截器实现的数据库脱敏；敏感词条、Spring配置文件等内容进行自定义格式数据脱敏，使用简单方便、易于扩展。[详细开发文档](https://gitee.com/eternalstone/SensitiveBye/wikis)



---



## 2、功能概述

1. java版本基准：jdk1.8

2. 支持Restful接口字段脱敏，java对象字段脱敏，支持jackson和fastjson序列化字段脱敏

3. 支持基于mybatis的数据库字段加解密脱敏

4. 支持常用日志框架输出脱敏，例如logback，log4j2

5. 支持SpringBoot配置文件配置项脱敏

6. 内置基于[ AhoCorasickDoubleArrayTrie ](https://github.com/hankcs/AhoCorasickDoubleArrayTrie)实现的敏感词库 

   
---





## 3、使用

#### 3.1 导入 

##### 3.1.1 SpringBoot项目导入

```xml
<dependency>
  <groupId>io.github.eternalstone</groupId>
  <artifactId>sensitivebye-spring-boot-starter</artifactId>
  <version>1.0.3</version>
</dependency>
```



##### 3.1.2 SpringMVC或其他java项目带入

```xml
<dependency>
  <groupId>io.github.eternalstone</groupId>
  <artifactId>sensitivebye-core</artifactId>
  <version>1.0.3</version>
</dependency>
```

> ​	包导不下来需要添加以下maven中央仓库：
>
> ```xml
> <repositories>
>    <repository>
>       <id>maven-central</id>
>       <name>Central Repository</name>
>       <url>https://repo1.maven.apache.org/maven2</url>
>    </repository>
> </repositories>
> ```



#### 3.2 配置

​		在SpringBoot项目中，在`Application`启动类上面加入`@EnableGlobalSensitiveBye`注解用来开启SensitiveBye自动装配。`@EnableGlobalSensitiveBye`注解可视为SensitiveBye所有功能是否生效的<font style="red">总开关</font>。

```java
@EnableGlobalSensitiveBye
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

​		SensitiveBye集成了以下配置

~~~yaml
sensitive-bye:
  field:
    enabled: true #默认为true, 开启字段脱敏开关
  log:
    enabled: false #默认为false, 开启日志脱敏开关
  mybatis:
    enabled: false #默认为false, 开启mybatis数据库脱敏开关
~~~

> ​	当开启对应开关时，需要导入相关的依赖，例如，开启log开关需要依赖logback或者log4j2相关的maven坐标，开启mybatis开关需要依赖mybatis或者基于mybatis开发的框架的maven坐标。



#### 3.3 字段脱敏

​		`SensitiveBye`字段脱敏的组件是`SensitiveFieldProvider`，SpringBoot引入starter包配合@EnableGlobalSensitiveBye注解将此组件自动注入，其他java项目引入core包则需要初始化此组件：

```java
@Bean
public SensitiveFieldProvider sensitiveFieldProvider(){
    return SensitiveFieldProvider.instance();
}
```

​		在需要脱敏java对象字段上注解`@SensitiveBye`，填入对应的脱敏规则即可：

```java
@SensitiveBye(strategy = SensitiveType.MOBILE)
private String mobile;
```

##### 3.3.1 接口字段脱敏

​		SpringMVC的接口序列化是基于jackson实现的，SensitiveBye已完成对jackson序列化的脱敏，所有进行以上配置后接口字段即可自动脱敏。



##### 3.3.2 json序列化脱敏

- jackson序列化脱敏

  ```java
  ObjectMapper mapper = new ObjectMapper();
  LOGGER.info("jackson序列化脱敏:{}", mapper.writeValueAsString(user));
  ```

- fastjson序列化脱敏

  ```java
   //fastjson序列化, 需要添加一个fastjson的值过滤器，SensitiveBye已经内置实现了SensitiveByeFilter
  LOGGER.info("fastjson序列化脱敏:{}", JSONObject.toJSONString(user, SensitiveByeFilter.instance()));	
  ```



##### 3.3.3 java对象脱敏

```java
SensitiveFieldProvider.instance().handle(SensitiveType.MOBILE, "13100001111", "*")
```



##### 3.3.4 自定义字段脱敏策略

​		Spring项目的自定义字段脱敏策略可以直接Bean一个CustomeFieldStrategy对象：

```java
@Bean
public CustomeFieldStrategy customeFieldStrategy(){
    CustomeFieldStrategy strategy = new CustomeFieldStrategy();
    //自定义策略key=test, var1表示原始值，var2表示脱敏符号, 后面的表达式即是自定义脱敏逻辑
    strategy.add("test", (var1, var2)-> var1.concat(var2));
    return strategy;
}
```

​		其他java项目需要给SensitiveFieldProvider设置自定义策略：

```java
CustomeFieldStrategy strategy = new CustomeFieldStrategy();
strategy.add("test", (var1, var2)-> var1.concat(var2));
SensitiveFieldProvider instance = SensitiveFieldProvider.instance();
instance.setCustomeStrategy(strategy);
```

​		添加的'test'自定义策略直接在注解中使用即可：@SensitiveBye("test")



#### 3.4 日志脱敏

​		`SensitiveBye`日志脱敏的组件是`SensitiveLogProvider`，SpringBoot项目配置`sensitive-bye.log.enabled=true`自动注入此组件，其他java项目需要初始化此组件：

```java
@Bean
public SensitiveLogProvider sensitiveFieldProvider(){
    SensitiveLogProvider sensitiveLogProvider = SensitiveLogProvider.instance();
    //如果存在自定义策略,可以设置一个SensitiveRule对象
    sensitiveLogProvider.setSensitiveRule();
    return sensitiveLogProvider
}
```

##### 3.4.1 logback日志脱敏

​		在logback.xml中添加如下配置即可：

```xml
<conversionRule conversionWord="msg" converterClass ="LogbackSensitiveConverter"/>
```



##### 3.4.2 log4j2日志脱敏

​		在log4j2-spring.xml中，原日志内容格式为 %msg，需要将其替换为%sdmsg。例如：

```xml
<appenders>
  <console name="STDOUT" target="SYSTEM_OUT">
    <patternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level ---- [%thread] %logger Line:%-3L - %sdmsg%n" />
  </console>
</appenders>
```



##### 3.4.3 自定义日志脱敏规则

​		SensitiveBye集成的默认日志脱敏规则见枚举类：LoggerRule。

​		如需添加或删除或自定义脱敏规则，实现`ISensitiveLogRule`接口的`custome(Map<String, SensitiveLogRuleWrapper> ruleMap)`方法即可，例如：

```java
@Component
public class CustomeLogRule implements ISensitiveLogRule {
    @Override
    public void custome(Map<String, SensitiveLogRuleWrapper> ruleMap) {
        SensitiveLogRuleWrapper wrapper = new SensitiveLogRuleWrapper();
        //规则名称
        wrapper.setName("wechat");
        //规则前缀匹配词
        wrapper.setKeys(new HashSet<String>(){{
            add("微信");
            add("wechat");
        }});
        //规则匹配词与匹配值之间的分隔符
        wrapper.setSeparators(new HashSet<String>(){{
            add("=");
            add(":");
            add("\\[");
        }});
        //正则表达式
        wrapper.setPattern(Pattern.compile("^[a-zA-Z]{1}[-_a-zA-Z0-9]{5,19}+$"));
        //替换表达式，注意需要带上匹配词和分隔符的占位符
        wrapper.setReplacement("$1$2$3*******");
        //新增规则
        ruleMap.put(wrapper.getName(), wrapper);
        //或者移除默认规则
        ruleMap.remove(LoggerRule.BANK_CARD.name().toLowerCase());
    }
}
```





#### 3.5 基于mybatis拦截器的数据库字段脱敏

​		`SensitiveBye`的mybatis脱敏组件是`MybatisSensitiveInterceptor`，它是基于Mybatis拦截器实现的。SpringBoot项目配置`sensitive-bye.mybatis.enabled=true`自动注入此组件，其他java项目需要初始化此组件：

```java
@Bean
public MybatisSensitiveInterceptor mybatisSensitiveInterceptor() {
	return new MybatisSensitiveInterceptor();
}
```

​		mybatis数据库字段脱敏用到了两个核心注解`@EnableCipher`和`@CipherField`:

```java
//@EnableCipher作用于Mapper接口的方法上，标注入参是加密还是解密，返回值是加密还是解密
@Mapper
public interface UserMapper {
    @EnableCipher(parameter = CipherType.ENCRYPT)
    int insertAndReturnId(User user);
    
    @EnableCipher(result = CipherType.DECRYPT)
    User selectById(@Param("id") Integer id);
}

//@CipherField作用于对象字段上，标注此字段需要加解密，并且指定加解密算法,加解密算法需要实现ICipherAlgorithm接口
public class User
    @CipherField(PasswordAlgorithm.class)
    private String password;
	@CipherField(MobileAlgorithm.class)
    private String mobile;
}
```

>  ​	1.@SensitiveBye注解和@CipherField注解虽然都是标注在对象属性上的，但是两个注解的作用互不影响，可以叠加使用，例如手机号从数据库密文查出来解密成明文，再用@SensitiveBye(strategy = SensitiveType.MOBILE)将明文手机号打上掩码。

> ​	2.如果项目中存在多个Mybatis拦截器，需要指定拦截器的执行顺序，可以写个配置类：

```java
@Configuration
public class MybatisConfig {
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
       return new ConfigurationCustomizer() {
           @Override
           public void customize(Configuration configuration) {
                configuration.addInterceptor(new MybatisInterceptor());
           }
       };
    }
}
```



#### 3.6 其他工具使用

##### 3.6.1 敏感词库组件

​		`SensitiveBye`的敏感词组件是`SensitiveWordProvider`，默认不自动注入，需要使用的时候初始化即可：

```java
@Bean
public SensitiveWordProvider sensitiveWordProvider(){
    return new SensitiveWordProvider();
}
```

​		SensitiveWordProvider提供了一个有参构造器，用于以不同的方式获取词库，SensitiveBye内置了两种方式：

- SensitiveWordSourceFromResource (获取resource目录下的sensitive.txt文件, 可自定义文件名)
- SensitiveWordSourceFromUrl(传入一个url，从网络获取词库文件)

​		你可以通过实现`ISensitiveWordSource`接口的loadSource()自定义获取词库的方式。

​		SensitiveWordProvider提供了三个方法：

```java
//handle方法用于将传入的字符串中的敏感词替换成输入的符号
String handle(String word, String symbol);
//contain方法用于检测传入的字符串中包含的敏感词组
List<String> contain(String word);
//reload方法用于重新载入词库
void reload();
```



##### 3.6.2 SpringBoot配置文件静态脱敏工具类

​		`SensitiveBye`实现了对SpringBoot的配置文件相关的配置项进行打掩码的工具`SensitiveFileUtil`， 支持对yml, yaml, properties三种配置文件，它提供了以下几个方法：

```java
//将source路径的配置文件进行配置项脱敏后输出到target目录
public static void sensitiveByeToFile(String source, String target);

//将source路径的配置文件进行配置项脱敏后输出到target目录,可传入handler自定义实现对配置项自定义操作
public static void sensitiveByeToFile(String source, String target, IFileHandler handler);

//将source路径的配置文件进行配置项脱敏后输出成字符串
public static String sensitiveByeToString(String source);

//将source路径的配置文件进行配置项脱敏后输出成字符串，可传入handler自定义实现对配置项自定义操作
public static String sensitiveByeToString(String source, IFileHandler handler);

```

​		SensitiveFileUtil对配置项脱敏的处理器是`SensitiveFileHandler`，它是默认的实现，你可以继承`AbstractFileHandler`类实现doFilter()对配置项进行操作：

```java
public class SensitiveCustomeFilterHandler extends AbstractFileHandler {
    @Override
    public void doFilter(LinkedHashMap<String, Object> param) {
        //删除test配置项
        param.remove("test");
    }
}

```

​		你可以将自定义的handler加入SensitiveFileHandler的后续执行链中，也可以直接传递自定义handler跳过SensitiveBye的SensitiveFileHandler的实现

```java
SensitiveFileHandler handler = new SensitiveFileHandler();
handler.setNextHandler(new SensitiveCustomeFilterHandler());
String s2 = SensitiveFileUtil.sensitiveByeToString(source, handler);
```



​		

---



## 4.引文

https://pagehelper.github.io/docs/interceptor/



---

## 联系方式
1. 邮箱联系： senstivebye@163.com，欢迎通过此邮件讨论与SensitiveBye相关的一切。