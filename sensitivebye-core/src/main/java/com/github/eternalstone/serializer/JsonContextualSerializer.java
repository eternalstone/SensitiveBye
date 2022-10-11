package com.github.eternalstone.serializer;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.lang.annotation.Annotation;

/**
 * to do something
 *
 * @author Justzone on 2022/8/30 19:53
 */
public interface JsonContextualSerializer extends ContextualSerializer {

    @Override
    default JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
        return null != property ? this.getJsonSerializer(provider, property) : provider.findNullValueSerializer(null);
    }

    default <A extends Annotation> A getAnnotation(BeanProperty property, Class<A> clazz) {
        A a = property.getAnnotation(clazz);
        if (null == a) {
            a = property.getContextAnnotation(clazz);
        }
        return a;
    }

    JsonSerializer<?> getJsonSerializer(SerializerProvider provider, BeanProperty property) throws JsonMappingException;

}
