package org.qfox.jestful.swagger.properties;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.properties.Property;

import java.lang.reflect.Type;

/**
 * Created by yangchangpei on 17/6/6.
 */
public interface PropertyConversionProvider {

    Property convert(Type type, ApiModelProperty property) throws Exception;

}
