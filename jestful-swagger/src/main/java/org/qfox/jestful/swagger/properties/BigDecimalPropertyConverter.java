package org.qfox.jestful.swagger.properties;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.Property;

import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * Created by yangchangpei on 17/6/6.
 */
public class BigDecimalPropertyConverter extends AbstractPropertyConverter implements PropertyConverter {
    @Override
    public boolean supports(Type type) {
        return BigDecimal.class == type;
    }

    @Override
    public Property convert(Type type, ApiModelProperty property, PropertyConversionProvider provider) throws Exception {
        Property p = new DoubleProperty();

        convert(property, p);

        return p;
    }
}
