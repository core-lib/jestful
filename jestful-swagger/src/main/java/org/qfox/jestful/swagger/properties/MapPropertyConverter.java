package org.qfox.jestful.swagger.properties;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by yangchangpei on 17/6/6.
 */
public class MapPropertyConverter extends AbstractPropertyConverter implements PropertyConverter {
    @Override
    public boolean supports(Type type) {
        Class<?> clazz;
        if (type instanceof Class<?>) clazz = (Class<?>) type;
        else if (type instanceof ParameterizedType) clazz = (Class<?>) ((ParameterizedType) type).getRawType();
        else return false;
        return Map.class.isAssignableFrom(clazz);
    }

    @Override
    public Property convert(Type type, ApiModelProperty property, PropertyConversionProvider provider) throws Exception {
        Type itemType = Object.class;
        if (type instanceof Class<?>) itemType = Object.class;
        else if (type instanceof ParameterizedType) itemType = ((ParameterizedType) type).getActualTypeArguments()[1];
        Property p = new MapProperty(provider.convert(itemType, null));
        convert(property, p);
        return p;
    }
}
