package org.qfox.jestful.swagger.properties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by yangchangpei on 17/6/5.
 */
public class ObjectPropertyConverter implements PropertyConverter {

    @Override
    public boolean supports(Type type) {
        return true;
    }

    @Override
    public Property convert(Type type, ApiModelProperty amp) {
        Class<?> clazz = null;
        if (type instanceof Class<?>) clazz = (Class<?>) type;
        if (type instanceof ParameterizedType) clazz = (Class<?>) ((ParameterizedType) type).getRawType();

        ApiModel am = clazz.getAnnotation(ApiModel.class);
        ObjectProperty p = new ObjectProperty();
        p.setName(clazz.getName());

        p.setName(am == null || am.value().trim().equals("") ? p.getName() : am.value());
        p.setDescription(am == null || am.description().trim().equals("") ? p.getDescription() : am.description());

        p.setName(amp == null || amp.name().trim().equals("") ? p.getName() : amp.name());
        p.setDescription(amp == null || amp.value().trim().equals("") ? p.getDescription() : amp.value());
        p.setRequired(amp == null ? p.getRequired() : amp.required());
        p.setPosition(amp == null ? p.getPosition() : amp.position());
        p.setAllowEmptyValue(amp == null ? p.getAllowEmptyValue() : amp.allowEmptyValue());
        p.setReadOnly(amp == null ? p.getReadOnly() : amp.readOnly());
        p.setAccess(amp == null ? p.getAccess() : amp.access());
        p.setExample(amp == null ? p.getExample() : amp.example());



        return null;
    }
}
