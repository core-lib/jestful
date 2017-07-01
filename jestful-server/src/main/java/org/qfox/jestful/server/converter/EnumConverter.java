package org.qfox.jestful.server.converter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.Map;

public class EnumConverter implements Converter {

    public boolean supports(Class<?> clazz) {
        return clazz.isEnum();
    }

    public <T> T convert(String name, Class<T> clazz, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(EnumMatcher.class)) continue;
            if (!Modifier.isStatic(method.getModifiers())) continue;
            if (method.getReturnType() != clazz) continue;
            if (method.getGenericParameterTypes().length != 1) continue;
            Type type = method.getGenericParameterTypes()[0];
            Object value = provider.convert(name, type, decoded, charset, map);
            try {
                return clazz.cast(method.invoke(null, value));
            } catch (Exception e) {
                throw new IncompatibleConversionException(e, name, clazz, map, provider);
            }
        }

        String[] values = map.get(name) != null ? map.get(name).clone() : null;
        String value = values != null && values.length > 0 ? values[0] : null;
        if (value == null) {
            return null;
        }
        if (decoded == false) {
            value = URLDecoder.decode(value, charset);
        }
        try {
            Object result = clazz.getMethod("valueOf", String.class).invoke(null, value);
            return clazz.cast(result);
        } catch (Exception e) {
            throw new IncompatibleConversionException(e, name, clazz, map, provider);
        }
    }

    public boolean supports(ParameterizedType type) {
        return false;
    }

    public Object convert(String name, ParameterizedType type, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        throw new UnsupportedOperationException("converter of " + this.getClass() + " do not supported parameterized type");
    }

}
