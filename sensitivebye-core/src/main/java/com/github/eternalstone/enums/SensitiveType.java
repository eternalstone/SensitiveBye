package com.github.eternalstone.enums;

import java.util.function.Function;

/**
 * 内置脱敏策略
 *
 * @author Justzone on 2022/8/29 18:44
 */
public enum SensitiveType {

    /**
     * 中文姓名
     */
    CHINESE_NAME,

    /**
     * 身份证号
     */
    ID_CARD,

    /**
     * 密码
     */
    PASSWORD,

    /**
     * 手机号
     */
    MOBILE,

    /**
     * 固话号码
     */
    PHONE,

    /**
     * 邮箱
     */
    EMAIL,

    /**
     * 地址
     */
    ADDRESS,

    /**
     * 银行卡号
     */
    BANK_CARD,

    /**
     * 车牌号
     */
    CAR_NUMBER,

    /**
     * 自定义策略
     */
    CUSTOME,

    ;

}
