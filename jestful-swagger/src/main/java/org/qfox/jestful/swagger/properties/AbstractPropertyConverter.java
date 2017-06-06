package org.qfox.jestful.swagger.properties;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.properties.Property;

/**
 * Created by yangchangpei on 17/6/6.
 */
public class AbstractPropertyConverter {

    protected void convert(ApiModelProperty property, Property p) {
        if (property != null && property.name().trim().length() > 0) p.setName(property.name());
        if (property != null && property.value().trim().length() > 0) p.setDescription(property.value());
        if (property != null) p.setRequired(property.required());
        if (property != null) p.setPosition(property.position());
        if (property != null) p.setAllowEmptyValue(property.allowEmptyValue());
        if (property != null) p.setReadOnly(p.getReadOnly());
        if (property != null && property.access().trim().length() > 0) p.setAccess(p.getAccess());
        if (property != null && property.example().trim().length() > 0) p.setExample((Object) property.example());
    }

}
