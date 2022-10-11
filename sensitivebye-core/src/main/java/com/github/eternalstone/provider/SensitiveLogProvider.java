package com.github.eternalstone.provider;

import com.github.eternalstone.attachment.log.ISensitiveLogRule;
import com.github.eternalstone.attachment.log.converter.ISensitiveConverter;
import com.github.eternalstone.enums.LoggerRule;
import com.github.eternalstone.enums.LoggerType;
import com.github.eternalstone.provider.model.SensitiveLogRuleWrapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 日志脱敏组件
 *
 * @author Justzone on 2022/9/29 10:59
 */

public class SensitiveLogProvider implements ISensitiveLogProvider {

    private static volatile SensitiveLogProvider INSTANCE;
    private final Map<String, SensitiveLogRuleWrapper> ruleMap = initRule();

    private SensitiveLogProvider() {
        buildLogRulePrefixs(ruleMap);
        LoggerType loggerType = getLoggerType();
        ISensitiveConverter converter = ISensitiveConverter.converterMap.get(loggerType);
        if (converter != null) {
            converter.setProvider(this);
        }
    }

    public void setSensitiveRule(ISensitiveLogRule sensitiveRule) {
        if (sensitiveRule != null) {
            sensitiveRule.custome(ruleMap);
            buildLogRulePrefixs(ruleMap);
        }
    }

    /**
     * 初始化默认日志规则
     * @return 返回规则map
     */
    private Map<String, SensitiveLogRuleWrapper> initRule() {
        LoggerRule[] rules = LoggerRule.values();
        Map<String, SensitiveLogRuleWrapper> ruleMap = new HashMap<>(rules.length);
        for (LoggerRule loggerRule : rules) {
            SensitiveLogRuleWrapper logRule = new SensitiveLogRuleWrapper(
                    loggerRule.name().toLowerCase(),
                    Arrays.stream(loggerRule.getKeys().split("\\|")).collect(Collectors.toSet()),
                    Arrays.stream(loggerRule.getSeparators().split("\\|")).collect(Collectors.toSet()),
                    Pattern.compile("(" + loggerRule.getKeys() + ")" + "(" + loggerRule.getSeparators() + ")" + loggerRule.getRegex()),
                    loggerRule.getReplacement()
            );
            ruleMap.put(logRule.getName(), logRule);
        }
        return ruleMap;
    }

    /**
     * 构建匹配规则前缀
     */
    private void buildLogRulePrefixs(Map<String, SensitiveLogRuleWrapper> ruleMap) {
        if (ruleMap.isEmpty()) {
            return;
        }
        for (SensitiveLogRuleWrapper logRule : ruleMap.values()) {
            logRule.buildPrefixs();
        }
    }

    public static SensitiveLogProvider instance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (SensitiveLogProvider.class) {
            if (INSTANCE != null) {
                return INSTANCE;
            }
            INSTANCE = new SensitiveLogProvider();
            return INSTANCE;
        }
    }


    @Override
    public String convert(String msg) {
        if (ruleMap.isEmpty()) {
            return msg;
        }
        for (SensitiveLogRuleWrapper logRule : ruleMap.values()) {
            //判断是否匹配前缀，不匹配快速返回
            if (logRule.isContain(msg)) {
                msg = logRule.getPattern().matcher(msg).replaceAll(logRule.getReplacement());
            }
        }
        return msg;
    }
}
