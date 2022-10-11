package com.github.eternalstone.util;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ReflectionUtils;

import java.util.Collection;
import java.util.Map;
/**
 * spring容器工具类
 *
 * @author Justzone on 2022/9/6 19:01
 */
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public SpringUtil() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(String name) {
        if (applicationContext == null) {
            return null;
        }
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        if (applicationContext == null) {
            return null;
        }
        T bean = null;
        Collection<T> beans = applicationContext.getBeansOfType(clazz).values();
        while (beans.iterator().hasNext()) {
            bean = beans.iterator().next();
            if (bean != null) {
                break;
            }
        }
        return bean;
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(name, clazz);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBeansOfType(type);
    }

    public static String[] getBeanNamesForType(Class<?> type) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBeanNamesForType(type);
    }

    public static String getProperty(String key) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getEnvironment().getProperty(key);
    }

    public static String[] getActiveProfiles() {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getEnvironment().getActiveProfiles();
    }

    public static <T> void registerBean(String beanName, T bean) {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext)applicationContext;
        context.getBeanFactory().registerSingleton(beanName, bean);
    }

}
