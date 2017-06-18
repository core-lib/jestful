package org.qfox.jestful.swagger.properties;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.UUIDProperty;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Created by yangchangpei on 17/6/6.
 */
public class UUIDPropertyConverter extends AbstractPropertyConverter implements PropertyConverter {
    @Override
    public boolean supports(Type type) {
        return UUID.class == type;
    }

    @Override
    public Property convert(Type type, ApiModelProperty property, PropertyConversionProvider provider) throws Exception {
        Property p = new UUIDProperty();

        convert(property, p);

        return p;
    }
}
