package com.github.eternalstone.example.sensitvebye;

import com.github.eternalstone.attachment.log.ISensitiveLogRule;
import com.github.eternalstone.enums.LoggerRule;
import com.github.eternalstone.provider.model.SensitiveLogRuleWrapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * to do something
 *
 * @author Justzone on 2022/10/10 21:17
 */
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
