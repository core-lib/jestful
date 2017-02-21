package org.qfox.jestful.server.converter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapConverter implements Converter {
    private final Map<Class<?>, Class<?>> implementations = new HashMap<Class<?>, Class<?>>();

    {
        implementations.put(Map.class, HashMap.class);
    }

    public boolean supports(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    public <T> T convert(String name, Class<T> clazz, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        Map<String, String[]> _map = new LinkedHashMap<String, String[]>();
        for (String key : map.keySet()) {
            if (key.startsWith(name + ".") && key.length() > name.length() + 1) {
                int index = key.indexOf('.');
                String _key = key.substring(index + 1);
                String[] values = map.get(key);
                if (decoded == false) {
                    _key = URLDecoder.decode(_key, charset);
                    for (int i = 0; values != null && i < values.length; i++) {
                        values[i] = URLDecoder.decode(values[i], charset);
                    }
                }
                _map.put(_key, values);
            }
        }
        Class<?> _class = clazz;
        while (implementations.containsKey(_class)) {
            _class = implementations.get(_class);
        }
        try {
            Object result = _class.getConstructor(Map.class).newInstance(_map);
            return clazz.cast(result);
        } catch (Exception e) {
            throw new UnsupportedConversionException(e, name, clazz, map, provider);
        }
    }

    public boolean supports(ParameterizedType type) {
        return type.getRawType() instanceof Class<?> && supports((Class<?>) type.getRawType());
    }

    public Object convert(String name, ParameterizedType type, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        Map<Object, Object> _map = new LinkedHashMap<Object, Object>();
        for (String key : map.keySet()) {
            if (key.startsWith(name + ".") && key.length() > name.length() + 1) {
                int index = key.indexOf('.');
                String _key = key.substring(index + 1);
                String[] values = map.get(key);

                String __key = _key.split("\\.")[0];

                Map<String, String[]> km = new LinkedHashMap<String, String[]>();
                km.put("key", new String[]{__key});
                Map<String, String[]> vm = new LinkedHashMap<String, String[]>();
                vm.put(_key, values);

                _map.put(provider.convert("key", type.getActualTypeArguments()[0], decoded, charset, km), provider.convert(__key, type.getActualTypeArguments()[1], decoded, charset, vm));
            }
        }
        Class<?> _class = (Class<?>) type.getRawType();
        while (implementations.containsKey(_class)) {
            _class = implementations.get(_class);
        }
        try {
            return _class.getConstructor(Map.class).newInstance(_map);
        } catch (Exception e) {
            throw new UnsupportedConversionException(e, name, type, map, provider);
        }
    }

}
