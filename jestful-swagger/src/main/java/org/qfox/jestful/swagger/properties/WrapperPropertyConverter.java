package org.qfox.jestful.swagger.properties;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.properties.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangchangpei on 17/6/6.
 */
public class WrapperPropertyConverter extends AbstractPropertyConverter implements PropertyConverter {
    private final Map<Class<?>, Class<? extends Property>> map = new HashMap<>();

    {
        map.put(Boolean.class, BooleanProperty.class);
        map.put(Byte.class, IntegerProperty.class);
        map.put(Character.class, IntegerProperty.class);
        map.put(Short.class, IntegerProperty.class);
        map.put(Integer.class, IntegerProperty.class);
        map.put(Long.class, LongProperty.class);
        map.put(Float.class, FloatProperty.class);
        map.put(Double.class, DoubleProperty.class);
    }

    @Override
    public boolean supports(Type type) {
        return type instanceof Class<?> && map.containsKey(type);
    }

    @Override
    public Property convert(Type type, ApiModelProperty property, PropertyConversionProvider provider) throws Exception {
        Class<?> clazz = (Class<?>) type;
        Property p = map.get(clazz).newInstance();

        convert(property, p);

        return p;
    }


}
