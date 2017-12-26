package org.qfox.jestful.commons;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Primaries {
    private static final Map<Class<?>, Class<?>> MAP = new LinkedHashMap<Class<?>, Class<?>>();

    static {
        MAP.put(boolean.class, Boolean.class);
        MAP.put(byte.class, Byte.class);
        MAP.put(short.class, Short.class);
        MAP.put(char.class, Character.class);
        MAP.put(int.class, Integer.class);
        MAP.put(long.class, Long.class);
        MAP.put(float.class, Float.class);
        MAP.put(double.class, Double.class);
    }

    public static boolean isPrimaryType(Class<?> type) {
        return MAP.containsKey(type);
    }

    public static Class<?> getWrapperType(Class<?> type) {
        if (MAP.containsKey(type)) return MAP.get(type);
        else throw new IllegalArgumentException(type + " is not a primary type");
    }

}
