package io.github.eternalstone.provider.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * to do something
 *
 * @author Justzone on 2022/9/29 16:19
 */
public class SensitiveLogRuleWrapper implements Serializable {

    /**
     * 规则名称
     */
    private String name;

    /**
     * 规则匹配关键字
     */
    private Set<String> keys = new HashSet<>();

    /**
     * 规则匹配分隔符
     */
    private Set<String> separators = new HashSet<>();

    /**
     * 规则匹配正则预编译对象
     */
    private Pattern pattern;

    /**
     * 正则替换表达式
     */
    private String replacement;

    /**
     * 匹配前缀集合
     */
    private Set<String> prefixs;

    public SensitiveLogRuleWrapper() {
    }

    public SensitiveLogRuleWrapper(String name, Set<String> keys, Set<String> separators, Pattern pattern, String replacement) {
        this.name = name;
        this.keys = keys;
        this.separators = separators;
        this.pattern = pattern;
        this.replacement = replacement;
    }

    /**
     * 判断文本中是否包含配置的关键词和分隔符
     *
     * @return
     */
    public boolean isContain(String str) {
        return prefixs.stream().anyMatch(prefix -> str.contains(prefix));
    }

    public boolean existsPrefixs(){
        return prefixs != null && prefixs.size() > 0;
    }

    public void buildPrefixs() {
        Set<String> prefixSet = new HashSet<>();
        keys.forEach(key -> {
            separators.forEach(separator -> {
                prefixSet.add(key.concat(separator));
            });
        });
        this.prefixs = prefixSet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getKeys() {
        return keys;
    }

    public void setKeys(Set<String> keys) {
        this.keys = keys;
    }

    public Set<String> getSeparators() {
        return separators;
    }

    public void setSeparators(Set<String> separators) {
        this.separators = separators;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }
}
