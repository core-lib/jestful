package org.qfox.jestful.swagger.properties;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.properties.DateProperty;
import io.swagger.models.properties.Property;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by yangchangpei on 17/6/6.
 */
public class DatePropertyConverter extends AbstractPropertyConverter implements PropertyConverter {
    @Override
    public boolean supports(Type type) {
        return type instanceof Class<?> && Date.class.isAssignableFrom((Class<?>) type);
    }

    @Override
    public Property convert(Type type, ApiModelProperty property, PropertyConversionProvider provider) throws Exception {
        Property p = new DateProperty();

        convert(property, p);

        return p;
    }
}
