package org.qfox.jestful.server.converter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by yangchangpei on 17/5/12.
 */
public class BigIntegerConverter implements Converter {

    @Override
    public boolean supports(Class<?> clazz) {
        return BigInteger.class == clazz;
    }

    @Override
    public <T> T convert(String name, Class<T> clazz, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        String[] values = map.get(name) != null ? map.get(name).clone() : null;
        String value = values != null && values.length > 0 ? values[0] : null;
        if (value == null) {
            return null;
        }
        if (!decoded) {
            value = URLDecoder.decode(value, charset);
        }
        return clazz.cast(new BigInteger(value));
    }

    @Override
    public boolean supports(ParameterizedType type) {
        return false;
    }

    @Override
    public Object convert(String name, ParameterizedType type, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        throw new UnsupportedOperationException("converter of " + this.getClass() + " do not supported parameterized type");
    }
}
