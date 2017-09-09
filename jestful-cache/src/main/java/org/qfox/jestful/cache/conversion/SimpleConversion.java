package org.qfox.jestful.cache.conversion;

import org.qfox.jestful.cache.Caching;
import org.qfox.jestful.cache.Conversion;
import org.qfox.jestful.cache.Converter;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yangchangpei on 17/9/5.
 */
public class SimpleConversion implements Conversion, Initialable {
    private final Set<KeyConverter> converters = new LinkedHashSet<KeyConverter>();
    private final Map<Class<? extends Converter<?>>, Converter<?>> cache = new ConcurrentHashMap<Class<? extends Converter<?>>, Converter<?>>();

    @Override
    public String convert(Object source) {
        if (source == null) return "null";
        for (KeyConverter converter : converters) if (converter.supports(source)) return converter.convert(source, this);
        if (source instanceof Caching) return "\"" + ((Caching) source).toCacheKey() + "\"";
        throw new IllegalArgumentException("could not convert instance of " + source.getClass() + " to cache key");
    }

    @Override
    public <T, C extends Converter<T>> C construct(Class<C> clazz) {
        if (cache.containsKey(clazz)) {
            return clazz.cast(cache.get(clazz));
        } else {
            try {
                C converter = clazz.newInstance();
                cache.put(clazz, converter);
                return converter;
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void initialize(BeanContainer beanContainer) {
        Map<String, KeyConverter> map = beanContainer.find(KeyConverter.class);
        converters.addAll(map.values());
    }
}
