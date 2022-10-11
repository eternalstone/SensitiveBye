package com.github.eternalstone.tools;


import com.github.eternalstone.function.TwoArgsFunction;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * to do something
 *
 * @author Justzone on 2022/8/31 14:28
 */
public class SensitiveFieldUtil {

    private static final String SKIP_SENSITIVE = "skip_sensitive";


    /**
     * 脱敏中文名
     */
    public static String formatChineseName(String value, String symbol) {
        return execute(value, symbol, (var1, var2)->
                value.length() > 3 ?
                StringUitl.rightPad(StringUitl.left(var1, 2), StringUitl.length(var1), var2) :
                StringUitl.rightPad(StringUitl.left(var1, 1), StringUitl.length(var1), var2));
    }

    /**
     * 脱敏身份证号码
     */
    public static String formatIdCard(String value, String symbol) {
        return execute(value, symbol, (var1, var2)-> StringUitl.leftPad(StringUitl.right(var1, 4), var1.length(), var2));
    }

    /**
     * 脱敏密码
     */
    public static String formatPassword(String value, String symbol) {
        return execute(value, symbol, (var1, var2) -> StringUitl.leftPad(StringUitl.left(var1, 0), var1.length(), var2));
    }

    /**
     * 脱敏手机号
     */
    public static String formatMobile(String value, String symbol) {
        return execute(value, symbol, (var1, var2)-> StringUitl.left(var1, 3)
                .concat(StringUitl.removeStart(StringUitl.leftPad(StringUitl.right(var1, 4), var1.length(), var2), 3)));
    }

    /**
     * 脱敏固定电话
     */
    public static String formatPhone(String value, String symbol) {
        int index = StringUitl.indexOf(value, "-");
        return execute(value, symbol, (var1, var2) -> index >= 0 ?
                StringUitl.left(var1, index + 1).concat(StringUitl.repeat(var2, 4).concat(StringUitl.right(var1, 4))) :
                StringUitl.leftPad(StringUitl.right(var1, 4), var1.length(), var2));
    }

    /**
     * 脱敏邮箱
     */
    public static String formatEmail(String value, String symbol) {
        int length = StringUitl.indexOf(value, "@");
        byte prefix = (byte) (length > 5 ? 3 : 1);
        return execute(value, symbol, (var1, var2) -> length == 1 ? var1 :
                StringUitl.rightPad(StringUitl.left(var1, prefix), length, var2).concat(StringUitl.right(var1, var1.length() - length)));
    }

    /**
     * 脱敏地址
     */
    public static String formatAddress(String value, String symbol) {
        int length = StringUitl.length(value);
        return execute(value, symbol, (var1, var2)-> length <= 8 ? var1 : StringUitl.rightPad(StringUitl.left(var1, length - 8), length, var2));
    }

    /**
     * 脱敏银行卡号
     */
    public static String formatBankCard(String value, String symbol) {
        return execute(value, symbol, (var1, var2) -> StringUitl.left(var1, 6).concat(
                StringUitl.removeStart(StringUitl.leftPad(StringUitl.right(var1, 4), var1.length(), var2),4)));
    }

    /**
     * 脱敏车牌号
     */
    public static String formatCarNumber(String value, String symbol) {
        return execute(value, symbol, (var1, var2) -> StringUitl.left(var1, 2).concat(
                StringUitl.removeStart(StringUitl.leftPad(StringUitl.right(var1, 1), var1.length(), var2),2)));
    }


    /**
     * 公共判空
     */
    private static String execute(String value, String symbol, TwoArgsFunction<String, String, String> func) {
        return StringUitl.isBlank(value) ? null : func.apply(value, symbol);
    }


    /**
     * 自定义字段跳过脱敏
     */
    public static void skipSensitive(String... field) {
        ThreadLocalHelper.put(SKIP_SENSITIVE, Arrays.stream(field).collect(Collectors.toSet()));
    }

    /**
     * 判断是否存在跳过字段设置
     *
     * @return
     */
    public static boolean isSkipSensitive(String field) {
        Set<String> fields = ThreadLocalHelper.get(SKIP_SENSITIVE);
        return fields != null && fields.contains(field);
    }

    /**
     * 移除跳过设置
     */
    public static void removeSkipSensitive(){
        ThreadLocalHelper.remove();
    }

}
