package com.github.eternalstone.provider;

import com.github.eternalstone.enums.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * to do something
 *
 * @author Justzone on 2022/9/29 10:59
 */
public interface ISensitiveLogProvider {

     Logger LOGGER = LoggerFactory.getLogger(ISensitiveLogProvider.class);

    /**
     * 获取当前项目所用的日志类型
     * @return
     */
    default LoggerType getLoggerType() {
        String className = "org.slf4j.impl.StaticLoggerBinder";
        try {
            Class<?> loggerBinderClass = Class.forName(className);
            URL location = loggerBinderClass.getProtectionDomain().getCodeSource().getLocation();
            if (location.getPath().contains(LoggerType.LOGBACK.getLog())) {
                return LoggerType.LOGBACK;
            }
            if (location.getPath().contains(LoggerType.LOG4j.getLog())) {
                return LoggerType.LOG4j;
            }
        } catch (Exception e) {
            LOGGER.error("org.slf4j.impl.StaticLoggerBinder not found");
        }
        return LoggerType.NONE;
    }

    /**
     * 转换日志内容
     * @param msg
     * @return
     */
    String convert(String msg);

}
