package org.qfox.jestful.swagger.properties;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.ByteArrayProperty;
import io.swagger.models.properties.Property;

import java.lang.reflect.Type;

/**
 * Created by yangchangpei on 17/6/6.
 */
public class ArrayPropertyConverter extends AbstractPropertyConverter implements PropertyConverter {
    @Override
    public boolean supports(Type type) {
        return type instanceof Class<?> && ((Class<?>) type).isArray();
    }

    @Override
    public Property convert(Type type, ApiModelProperty property, PropertyConversionProvider provider) throws Exception {
        Class<?> itemType = ((Class<?>) type).getComponentType();
        Property p = itemType == byte.class ? new ByteArrayProperty() : new ArrayProperty(provider.convert(itemType, null));
        convert(property, p);
        return p;
    }
}
