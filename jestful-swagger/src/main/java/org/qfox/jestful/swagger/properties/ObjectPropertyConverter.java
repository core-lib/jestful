package org.qfox.jestful.swagger.properties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by yangchangpei on 17/6/5.
 */
public class ObjectPropertyConverter extends AbstractPropertyConverter implements PropertyConverter, PropertyConversionProvider, Initialable {
    private final Set<PropertyConverter> converters = new LinkedHashSet<>();

    private final Map<Class<?>, String> cache = new LinkedHashMap<>();

    @Override
    public boolean supports(Type type) {
        return true;
    }

    @Override
    public Property convert(Type type, ApiModelProperty property, PropertyConversionProvider provider) throws Exception {
        Class<?> clazz;
        if (type instanceof Class<?>) clazz = (Class<?>) type;
        else if (type instanceof ParameterizedType) clazz = (Class<?>) ((ParameterizedType) type).getRawType();
        else throw new UnsupportedOperationException();

        if (cache.containsKey(clazz)) return new RefProperty(cache.get(clazz));

        ApiModel model = clazz.getAnnotation(ApiModel.class);
        ObjectProperty p = new ObjectProperty();
        p.setName(clazz.getName());

        p.setName(model == null || model.value().trim().equals("") ? p.getName() : model.value());
        p.setDescription(model == null || model.description().trim().equals("") ? p.getDescription() : model.description());

        convert(property, p);

        Map<String, Property> properties = new LinkedHashMap<>();
        PropertyDescriptor[] descriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            String name = descriptor.getName();
            if (name.equals("class")) continue;
            property = descriptor.getReadMethod().getAnnotation(ApiModelProperty.class);
            if (property != null && property.hidden()) continue;
            Type returnType = descriptor.getReadMethod().getReturnType();
            Property prop = provider.convert(returnType, property);
            properties.put(name, prop);
        }
        p.setProperties(properties);

        cache.put(clazz, p.getName());

        return p;
    }

    @Override
    public Property convert(Type type, ApiModelProperty property) throws Exception {
        for (PropertyConverter c : converters) if (c.supports(type)) return c.convert(type, property, this);
        throw new UnsupportedConversionException(type);
    }

    @Override
    public void initialize(BeanContainer beanContainer) {
        Map<String, PropertyConverter> map = beanContainer.find(PropertyConverter.class);
        converters.addAll(map.values());
        converters.remove(this);
        converters.add(this);
    }
}
