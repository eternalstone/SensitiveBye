package io.github.eternalstone.attachment.log.converter;

import io.github.eternalstone.enums.LoggerType;
import io.github.eternalstone.provider.SensitiveLogProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * to do something
 *
 * @author Justzone on 2022/9/29 11:18
 */
public interface ISensitiveConverter {

    Map<LoggerType, ISensitiveConverter> converterMap = new HashMap<>(1);

    void setProvider(SensitiveLogProvider provider);

    LoggerType getLoggerType();

}
