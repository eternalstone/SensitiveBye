package com.github.eternalstone.attachment.log;

import com.github.eternalstone.provider.model.SensitiveLogRuleWrapper;

import java.util.Map;

/**
 *
 * 日志匹配规则接口
 * @author Justzone on 2022/9/29 11:14
 */
public interface ISensitiveLogRule {

    void custome(Map<String, SensitiveLogRuleWrapper> ruleMap);

}
