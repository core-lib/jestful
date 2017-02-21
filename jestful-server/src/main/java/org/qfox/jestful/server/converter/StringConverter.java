package org.qfox.jestful.server.converter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.net.URLDecoder;
import java.util.Map;

public class StringConverter implements Converter {

    public boolean supports(Class<?> clazz) {
        return clazz == String.class;
    }

    public <T> T convert(String name, Class<T> clazz, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        String[] values = map.get(name) != null ? map.get(name).clone() : null;
        String value = values != null && values.length > 0 ? values[0] : null;
        return decoded ? clazz.cast(value) : clazz.cast(URLDecoder.decode(value, charset));
    }

    public boolean supports(ParameterizedType type) {
        return false;
    }

    public Object convert(String name, ParameterizedType type, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        throw new UnsupportedOperationException("converter of " + this.getClass() + " do not supported parameterized type");
    }

}
