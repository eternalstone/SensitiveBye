package io.github.eternalstone.attachment.log.converter;

import io.github.eternalstone.enums.LoggerType;
import io.github.eternalstone.provider.SensitiveLogProvider;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

/**
 * to do something
 *
 * @author Justzone on 2022/9/29 11:04
 */
@Plugin(name = "Log4jSensitiveConverter", category = PatternConverter.CATEGORY, printObject = true)
@ConverterKeys({"sdmsg"})
public class Log4jSensitiveConverter extends LogEventPatternConverter implements ISensitiveConverter {

    public static String DEFAULT_CONVERT_KEY = "sdmsg";
    private static Log4jSensitiveConverter INSTANCE;
    private SensitiveLogProvider provider;

    private Log4jSensitiveConverter() {
        super(DEFAULT_CONVERT_KEY, DEFAULT_CONVERT_KEY);
        converterMap.put(getLoggerType(), this);
    }

    public static Log4jSensitiveConverter newInstance(final String[] options) {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (Log4jSensitiveConverter.class) {
            if (INSTANCE != null) {
                return INSTANCE;
            }
            INSTANCE = new Log4jSensitiveConverter();
            return INSTANCE;
        }
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        if (provider == null) {
            toAppendTo.append(event.getMessage().getFormattedMessage());
        } else {
            toAppendTo.append(provider.convert(event.getMessage().getFormattedMessage()));
        }
    }

    @Override
    public void setProvider(SensitiveLogProvider provider) {
        this.provider = provider;
    }

    @Override
    public LoggerType getLoggerType() {
        return LoggerType.LOG4j;
    }
}
