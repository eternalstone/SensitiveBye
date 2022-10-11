package io.github.eternalstone.attachment.log.converter;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import io.github.eternalstone.enums.LoggerType;
import io.github.eternalstone.provider.SensitiveLogProvider;

/**
 * to do something
 *
 * @author Justzone on 2022/9/29 11:02
 */
public class LogbackSensitiveConverter extends MessageConverter implements ISensitiveConverter {

    private SensitiveLogProvider provider;

    public LogbackSensitiveConverter() {
        converterMap.put(getLoggerType(), this);
    }

    @Override
    public String convert(ILoggingEvent event) {
        if (provider == null) {
            return super.convert(event);
        }
        return provider.convert(event.getFormattedMessage());
    }

    @Override
    public void setProvider(SensitiveLogProvider provider) {
        this.provider = provider;
    }

    @Override
    public LoggerType getLoggerType() {
        return LoggerType.LOGBACK;
    }
}
