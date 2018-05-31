package org.qfox.jestful.server.converter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.net.URLDecoder;
import java.util.*;

public class CollectionConverter implements Converter {
    private final Map<Class<?>, Class<?>> implementations = new LinkedHashMap<Class<?>, Class<?>>();

    {
        implementations.put(Collection.class, List.class);
        implementations.put(List.class, ArrayList.class);
        implementations.put(Set.class, LinkedHashSet.class);
        implementations.put(SortedSet.class, TreeSet.class);
        implementations.put(Queue.class, PriorityQueue.class);
    }

    public boolean supports(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    public <T> T convert(String name, Class<T> clazz, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        String[] values = map.get(name) != null ? map.get(name).clone() : new String[0];
        for (int i = 0; !decoded && values != null && i < values.length; i++) {
            values[i] = values[i] == null ? null : URLDecoder.decode(values[i], charset);
        }
        Class<?> _class = clazz;
        while (implementations.containsKey(_class)) {
            _class = implementations.get(_class);
        }
        try {
            Object collection = _class.getConstructor(Collection.class).newInstance(values != null ? Arrays.asList(values) : Collections.emptyList());
            return clazz.cast(collection);
        } catch (Exception e) {
            throw new UnsupportedConversionException(e, name, clazz, map, provider);
        }
    }

    public boolean supports(ParameterizedType type) {
        return type.getRawType() instanceof Class<?> && supports((Class<?>) type.getRawType());
    }

    public Object convert(String name, ParameterizedType type, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        Class<?> _class = (Class<?>) type.getRawType();
        while (implementations.containsKey(_class)) {
            _class = implementations.get(_class);
        }
        Collection<Object> collection = new ArrayList<Object>();
        for (String key : map.keySet()) {
            if (key.equals(name) || (key.startsWith(name + ".") && key.length() > name.length() + 1)) {
                String[] values = map.get(key).clone();
                Map<String, String[]> _map = new LinkedHashMap<String, String[]>();
                String[] _values = new String[1];
                for (String value : values) {
                    _values[0] = value;
                    _map.put(key, _values);
                    collection.add(provider.convert(name, type.getActualTypeArguments()[0], decoded, charset, _map));
                    _map.clear();
                }
            }
        }
        try {
            return _class.getConstructor(Collection.class).newInstance(collection);
        } catch (Exception e) {
            throw new UnsupportedConversionException(e, name, type, map, provider);
        }
    }
}
