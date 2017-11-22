package org.qfox.jestful.server.converter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WrapperConverter implements Converter {
    private final List<Class<?>> wrappers = new ArrayList<Class<?>>();

    {
        wrappers.add(Boolean.class);
        wrappers.add(Byte.class);
        wrappers.add(Short.class);
        wrappers.add(Character.class);
        wrappers.add(Integer.class);
        wrappers.add(Long.class);
        wrappers.add(Float.class);
        wrappers.add(Double.class);
    }

    public boolean supports(Class<?> clazz) {
        return wrappers.contains(clazz);
    }

    public <T> T convert(String name, Class<T> clazz, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        String[] values = map.get(name) != null ? map.get(name).clone() : null;
        String value = values != null && values.length > 0 ? values[0] : null;
        if (value == null) {
            return null;
        }
        if (!decoded) {
            value = URLDecoder.decode(value, charset);
        }
        Object result = null;
        try {
            switch (wrappers.indexOf(clazz)) {
                case 0:
                    result = Boolean.valueOf(value);
                    break;
                case 1:
                    result = Byte.valueOf(value);
                    break;
                case 2:
                    result = Short.valueOf(value);
                    break;
                case 3:
                    result = value.charAt(0);
                    break;
                case 4:
                    result = Integer.valueOf(value);
                    break;
                case 5:
                    result = Long.valueOf(value);
                    break;
                case 6:
                    result = Float.valueOf(value);
                    break;
                case 7:
                    result = Double.valueOf(value);
                    break;
            }
        } catch (Exception e) {
            throw new IncompatibleConversionException(e, name, clazz, map, provider);
        }
        return clazz.cast(result);
    }

    public boolean supports(ParameterizedType type) {
        return false;
    }

    public Object convert(String name, ParameterizedType type, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        throw new UnsupportedOperationException("converter of " + this.getClass() + " do not supported parameterized type");
    }

}
