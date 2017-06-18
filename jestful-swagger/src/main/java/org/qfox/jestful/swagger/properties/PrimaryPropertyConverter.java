package org.qfox.jestful.swagger.properties;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.properties.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangchangpei on 17/6/6.
 */
public class PrimaryPropertyConverter extends AbstractPropertyConverter implements PropertyConverter {
    private final Map<Class<?>, Class<? extends Property>> map = new HashMap<>();

    {
        map.put(boolean.class, BooleanProperty.class);
        map.put(byte.class, IntegerProperty.class);
        map.put(char.class, IntegerProperty.class);
        map.put(short.class, IntegerProperty.class);
        map.put(int.class, IntegerProperty.class);
        map.put(long.class, LongProperty.class);
        map.put(float.class, FloatProperty.class);
        map.put(double.class, DoubleProperty.class);
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
