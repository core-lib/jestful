package org.qfox.jestful.server.converter;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanConverter implements Converter {

    public boolean supports(Class<?> clazz) {
        return clazz.isInterface() || clazz.isAnnotation() || clazz.isEnum() || clazz.isArray() || Modifier.isAbstract(clazz.getModifiers()) ? false : true;
    }

    public <T> T convert(String name, Class<T> clazz, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        T bean = null;
        try {
            bean = clazz.newInstance();
        } catch (Exception e) {
            throw new UnsupportedConversionException(e, name, clazz, map, provider);
        }
        for (String key : map.keySet()) {
            if (key.startsWith(name + ".") && key.length() > name.length() + 1) {
                int index = key.indexOf('.');
                String property = key.substring(index + 1);
                String[] values = map.get(key) != null ? map.get(key) : new String[0];
                Map<String, String[]> _map = new HashMap<String, String[]>();
                _map.put(property, values);
                try {
                    PropertyDescriptor descriptor = new PropertyDescriptor(property.split("\\.")[0], clazz);
                    Object value = provider.convert(property.split("\\.")[0], descriptor.getPropertyType(), decoded, charset, _map);
                    descriptor.getWriteMethod().invoke(bean, value);
                } catch (ConversionException e) {
                    throw e;
                } catch (Exception e) {
                    throw new UnknownConversionException(e, name, clazz, map, provider);
                }
            }
        }
        return bean;
    }

    public boolean supports(ParameterizedType type) {
        return type.getRawType() instanceof Class<?>;
    }

    public Object convert(String name, ParameterizedType type, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        Class<?> clazz = (Class<?>) type.getRawType();
        Object bean = null;
        try {
            bean = clazz.newInstance();
        } catch (Exception e) {
            throw new UnsupportedConversionException(e, name, type, map, provider);
        }
        List<?> variables = Arrays.asList(clazz.getTypeParameters());
        for (String key : map.keySet()) {
            if (key.startsWith(name + ".") && key.length() > name.length() + 1) {
                int index = key.indexOf('.');
                String property = key.substring(index + 1);
                String[] values = map.get(key) != null ? map.get(key) : new String[0];
                Map<String, String[]> _map = new HashMap<String, String[]>();
                _map.put(property, values);
                try {
                    PropertyDescriptor descriptor = new PropertyDescriptor(property.split("\\.")[0], clazz);
                    Type _type = descriptor.getReadMethod().getGenericReturnType();
                    if (_type instanceof TypeVariable<?>) {
                        int _index = variables.indexOf(_type);
                        _type = type.getActualTypeArguments()[_index];
                    }
                    Object value = provider.convert(property.split("\\.")[0], _type, decoded, charset, _map);
                    descriptor.getWriteMethod().invoke(bean, value);
                } catch (ConversionException e) {
                    throw e;
                } catch (Exception e) {
                    throw new UnknownConversionException(e, name, clazz, map, provider);
                }
            }
        }
        return bean;
    }

}
