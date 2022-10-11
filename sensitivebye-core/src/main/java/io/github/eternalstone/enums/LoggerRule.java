package io.github.eternalstone.enums;

/**
 * to do something
 *
 * @author Justzone on 2022/9/29 16:46
 */
public enum LoggerRule {

    /**
     * 姓名
     */
    CHINESE_NAME("姓名|真实姓名", "=|=\\[|='|\\\":\\\"|:|：|':'", "([\\u4e00-\\u9fa5]{1}+)([\\u4e00-\\u9fa5]{1,3}+)", "$1$2$3**"),

    /**
     * 身份证
     */
    ID_CARD("idcard|身份证|身份证号", "=|=\\[|='|\\\":\\\"|:|：|':'", "(\\d{1}+)(\\d{16}+)([\\d|X|x]{1}+)", "$1$2$3****************$5"),

    /**
     * 密码
     */
    PASSWORD("password|pwd|密码", "=|=\\[|='|\\\":\\\"|:|：|':'", "(\\d{6}+)", "$1$2******"),

    /**
     * 手机号
     */
    MOBILE("mobile|手机号|手机", "=|=\\[|='|\\\":\\\"|:|：|':'", "(1)([1-9]{2}+)(\\d{4}+)(\\d{4}+)", "$1$2$3$4****$6"),

    /**
     * 固定电话
     */
    PHONE("固定电话|座机", "=|=\\[|='|\\\":\\\"|:|：|':'", "([\\d]{3,4}-)(\\d{2}+)(\\d{4}+)(\\d{2}+)", "$1$2$3$4****$6"),

    /**
     * 邮箱
     */
    EMAIL("email|邮箱", "=|=\\[|='|\\\":\\\"|:|：|':'", "(\\w{1}+)(\\w*)(\\w{1}+)@(\\w+).com", "$1$2$3****$4@$5.com"),

    /**
     * 地址
     */
    ADDRESS("address|地址|家庭地址|详细地址", "=|=\\[|='|\\\":\\\"|:|：|':'", "([\\u4e00-\\u9fa5]{3}+)(\\w|[\\u4e00-\\u9fa5]|-)*", "$1$2$3****"),

    /**
     * 银行卡
     */
    BANK_CARD("bankCard|银行卡", "=|=\\[|='|\\\":\\\"|:|：|':'", "(\\d{15}+)(\\d{4}+)", "$1$2***************$4"),

    ;

    LoggerRule(String keys, String separators, String regex, String replacement) {
        this.keys = keys;
        this.separators = separators;
        this.regex = regex;
        this.replacement = replacement;
    }


    private String keys;

    private String separators;

    private String regex;

    private String replacement;

    public String getKeys() {
        return keys;
    }

    public String getSeparators() {
        return separators;
    }

    public String getRegex() {
        return regex;
    }

    public String getReplacement() {
        return replacement;
    }
}
