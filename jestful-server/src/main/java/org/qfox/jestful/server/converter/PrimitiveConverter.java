package org.qfox.jestful.server.converter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrimitiveConverter implements Converter {
    private final List<Class<?>> primaries = new ArrayList<Class<?>>();

    {
        primaries.add(boolean.class);
        primaries.add(byte.class);
        primaries.add(short.class);
        primaries.add(char.class);
        primaries.add(int.class);
        primaries.add(long.class);
        primaries.add(float.class);
        primaries.add(double.class);
    }

    public boolean supports(Class<?> clazz) {
        return primaries.contains(clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T convert(String name, Class<T> clazz, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        String[] values = map.get(name);
        String value = values != null && values.length > 0 ? values[0] : "0";
        if (decoded == false) {
            value = URLDecoder.decode(value, charset);
        }
        Object result = null;
        try {
            switch (primaries.indexOf(clazz)) {
                case 0:
                    result = value.equals("0") ? Boolean.FALSE : Boolean.valueOf(value);
                    break;
                case 1:
                    result = Byte.valueOf(value);
                    break;
                case 2:
                    result = Short.valueOf(value);
                    break;
                case 3:
                    result = Character.valueOf(value.charAt(0));
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
        return (T) result;
    }

    public boolean supports(ParameterizedType type) {
        return false;
    }

    public Object convert(String name, ParameterizedType type, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        throw new UnsupportedOperationException("converter of " + this.getClass() + " do not supported parameterized type");
    }

}
