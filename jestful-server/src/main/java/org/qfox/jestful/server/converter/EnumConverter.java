package org.qfox.jestful.server.converter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.net.URLDecoder;
import java.util.Map;

public class EnumConverter implements Converter {

    public boolean supports(Class<?> clazz) {
        return clazz.isEnum();
    }

    public <T> T convert(String name, Class<T> clazz, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        Object result = provider.getExtendConversionResult(name, clazz, decoded, charset, map);
        if (result != null) return clazz.cast(result);

        String[] values = map.get(name) != null ? map.get(name).clone() : null;
        String value = values != null && values.length > 0 ? values[0] : null;
        if (value == null) {
            return null;
        }
        if (decoded == false) {
            value = URLDecoder.decode(value, charset);
        }
        try {
            result = clazz.getMethod("valueOf", String.class).invoke(null, value);
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
