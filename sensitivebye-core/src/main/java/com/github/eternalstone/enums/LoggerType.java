package com.github.eternalstone.enums;

/**
 * to do something
 *
 * @author Justzone on 2022/9/29 11:07
 */
public enum LoggerType {

    NONE(null),

    LOGBACK("logback"),

    LOG4j("log4j");

    private String log;

    LoggerType(String log) {
        this.log = log;
    }

    public String getLog() {
        return log;
    }
}
