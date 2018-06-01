package org.qfox.jestful.commons;

import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-01 17:31
 **/
public class ConversionKit {

    public static Object convert(String name, Object obj, Type type, String key, String[] values) {
        // 名称相等 那么 类型应该是简单类型
        if (key.equals(name)) {
            if (type == null) throw new IllegalArgumentException();
            else if (type == boolean.class || type == Boolean.class) return Boolean.valueOf(values[0]);
            else if (type == byte.class || type == Byte.class) return Byte.valueOf(values[0]);
            else if (type == short.class || type == Short.class) return Short.valueOf(values[0]);
            else if (type == char.class || type == Character.class) return values[0].charAt(0);
            else if (type == int.class || type == Integer.class) return Integer.valueOf(values[0]);
            else if (type == float.class || type == Float.class) return Float.valueOf(values[0]);
            else if (type == long.class || type == Long.class) return Long.valueOf(values[0]);
            else if (type == double.class || type == Double.class) return Double.valueOf(values[0]);
            else if (type == BigDecimal.class) return new BigDecimal(values[0]);
            else if (type == BigInteger.class) return new BigInteger(values[0]);
            else if (type == AtomicBoolean.class) return new AtomicBoolean(Boolean.valueOf(values[0]));
            else if (type == AtomicInteger.class) return new AtomicInteger(Integer.valueOf(values[0]));
            else if (type == AtomicLong.class) return new AtomicLong(Long.valueOf(values[0]));
            else if (type == String.class) return values[0];
            else if (type == StringBuilder.class) return new StringBuilder(values[0]);
            else if (type == StringBuffer.class) return new StringBuffer(values[0]);
            else if (type == StringWriter.class) return new StringWriter().append(values[0]);
            else if (type instanceof Class<?> && ((Class<?>) type).isArray()) {
                Class<?> clazz = (Class<?>) type;
                if (obj == null) obj = Array.newInstance(clazz.getComponentType(), 0);
                int length = Array.getLength(obj);
                for (String value : values) {
                    Object e = convert("", null, clazz.getComponentType(), "", new String[]{value});
                    Object arr = Array.newInstance(clazz.getComponentType(), length + 1);
                    System.arraycopy(obj, 0, arr, 0, length);
                    Array.set(arr, length, e);
                    obj = arr;
                }
            }
        }
        return obj;
    }

}
