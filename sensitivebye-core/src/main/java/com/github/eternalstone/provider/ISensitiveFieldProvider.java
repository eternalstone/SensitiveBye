package com.github.eternalstone.provider;


import com.github.eternalstone.enums.SensitiveType;
import com.github.eternalstone.tools.SensitiveFieldUtil;
import com.github.eternalstone.provider.model.SensitiveFieldWrapper;
import com.github.eternalstone.provider.strategy.DefaultFieldStrategy;
import com.github.eternalstone.provider.strategy.IFieldStrategy;

/**
 * to do something
 *
 * @author Justzone on 2022/8/30 14:57
 */
public interface ISensitiveFieldProvider {

    default IFieldStrategy getDefaultStrategy(){
        return new DefaultFieldStrategy()
                .add(SensitiveType.CHINESE_NAME, (var1, var2) -> SensitiveFieldUtil.formatChineseName(var1, var2))
                .add(SensitiveType.ID_CARD, (var1, var2) -> SensitiveFieldUtil.formatIdCard(var1, var2))
                .add(SensitiveType.PASSWORD, (var1, var2) -> SensitiveFieldUtil.formatPassword(var1, var2))
                .add(SensitiveType.MOBILE, (var1, var2) -> SensitiveFieldUtil.formatMobile(var1, var2))
                .add(SensitiveType.PHONE, (var1, var2) -> SensitiveFieldUtil.formatPhone(var1, var2))
                .add(SensitiveType.EMAIL, (var1, var2) -> SensitiveFieldUtil.formatEmail(var1, var2))
                .add(SensitiveType.ADDRESS, (var1, var2) -> SensitiveFieldUtil.formatAddress(var1, var2))
                .add(SensitiveType.BANK_CARD, (var1, var2) -> SensitiveFieldUtil.formatBankCard(var1, var2))
                .add(SensitiveType.CAR_NUMBER, (var1, var2) -> SensitiveFieldUtil.formatCarNumber(var1, var2));
    }



    String handle(String value, SensitiveFieldWrapper wrapper);

}
