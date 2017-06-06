package org.qfox.jestful.swagger.properties;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;

import java.lang.reflect.Type;

/**
 * Created by yangchangpei on 17/6/6.
 */
public class StringPropertyConverter extends AbstractPropertyConverter implements PropertyConverter {
    @Override
    public boolean supports(Type type) {
        return type instanceof Class<?> && CharSequence.class.isAssignableFrom((Class<?>) type);
    }

    @Override
    public Property convert(Type type, ApiModelProperty property, PropertyConversionProvider provider) throws Exception {
        Property p = new StringProperty();

        convert(property, p);

        return p;
    }
}
