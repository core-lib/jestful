package org.qfox.jestful.swagger.models;

import io.swagger.models.Model;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.swagger.properties.PropertyConversionProvider;

import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/**
 * Created by yangchangpei on 17/6/6.
 */
public class SwaggerModelConverter implements ModelConverter, ModelConversionProvider, Initialable {
    private final Set<ModelConverter> converters = new LinkedHashSet<>();

    private PropertyConversionProvider propertyConversionProvider;

    @Override
    public boolean supports(Type type) {
        return true;
    }

    @Override
    public Model convert(Type type, ModelConversionProvider provider) throws Exception {
        Property property = propertyConversionProvider.convert(type, null);
        return PropertyBuilder.toModel(property);
    }

    @Override
    public Model convert(Type type) throws Exception {
        for (ModelConverter converter : converters) if (converter.supports(type)) return converter.convert(type, this);
        return null;
    }

    @Override
    public void initialize(BeanContainer beanContainer) {
        Map<String, ModelConverter> map = beanContainer.find(ModelConverter.class);
        converters.addAll(map.values());
        converters.remove(this);
        converters.add(this);

        propertyConversionProvider = beanContainer.get(PropertyConversionProvider.class);
    }
}
