package io.github.eternalstone.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.eternalstone.annotation.SensitiveBye;
import io.github.eternalstone.provider.ISensitiveFieldProvider;
import io.github.eternalstone.provider.model.SensitiveFieldWrapper;
import io.github.eternalstone.tools.SensitiveFieldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * to do something
 *
 * @author Justzone on 2022/8/29 11:46
 */
public class SensitiveSerializer extends JsonSerializer<String> implements JsonContextualSerializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveSerializer.class);
    private static ISensitiveFieldProvider SENSITIVE_FIELD_PROVIDER;
    private SensitiveFieldWrapper sensitiveFieldWrapper;

    public SensitiveSerializer() {
    }

    public SensitiveSerializer(SensitiveFieldWrapper sensitiveFieldWrapper) {
        this.sensitiveFieldWrapper = sensitiveFieldWrapper;
    }

    public static void setSensitiveFieldProvider(ISensitiveFieldProvider sensitiveFieldProvider) {
        SENSITIVE_FIELD_PROVIDER = sensitiveFieldProvider;
    }

    public static ISensitiveFieldProvider getSensitiveFieldProvider() {
        return SENSITIVE_FIELD_PROVIDER;
    }


    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (SENSITIVE_FIELD_PROVIDER == null) {
            LOGGER.warn("You used the annotation `@SensitiveBye` but did not inject `SensitiveFieldProvider`");
            gen.writeObject(value);
        } else {
            if (SensitiveFieldUtil.isSkipSensitive(sensitiveFieldWrapper.getName())) {
                gen.writeObject(value);
            } else {
                gen.writeObject(SENSITIVE_FIELD_PROVIDER.handle(value, sensitiveFieldWrapper));
            }
        }
    }

    @Override
    public JsonSerializer<?> getJsonSerializer(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
        if (Objects.equals(property.getType().getRawClass(), String.class)) {
            SensitiveBye annotation = this.getAnnotation(property, SensitiveBye.class);
            if (null != annotation) {
                return new SensitiveSerializer(new SensitiveFieldWrapper(annotation, property.getName()));
            }
        }
        return provider.findValueSerializer(property.getType(), property);
    }
}
