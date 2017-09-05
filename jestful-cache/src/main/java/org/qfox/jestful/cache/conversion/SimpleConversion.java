package org.qfox.jestful.cache.conversion;

import org.qfox.jestful.cache.CacheKey;
import org.qfox.jestful.cache.Conversion;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by yangchangpei on 17/9/5.
 */
public class SimpleConversion implements Conversion, Initialable {
    private final Set<KeyConverter> converters = new LinkedHashSet<>();

    @Override
    public String convert(Object source) {
        if (source == null) return "null";
        for (KeyConverter converter : converters) if (converter.supports(source)) return converter.convert(source, this);
        if (source instanceof CacheKey) return "\"" + ((CacheKey) source).toStringKey() + "\"";
        throw new IllegalArgumentException("could not convert instance of " + source.getClass() + " to cache key");
    }

    @Override
    public void initialize(BeanContainer beanContainer) {
        Map<String, KeyConverter> map = beanContainer.find(KeyConverter.class);
        converters.addAll(map.values());
    }
}
