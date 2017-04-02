package org.qfox.jestful.commons;

import java.lang.reflect.Array;

/**
 * Created by yangchangpei on 16/9/21.
 */
public class ArrayKit {

    public static <T> T[] copyOf(T[] original, int newLength) {
        Class<?> type = original.getClass();
        @SuppressWarnings("unchecked")
        T[] array = (type == Object[].class)
                ? (T[]) new Object[newLength]
                : (T[]) Array.newInstance(type.getComponentType(), newLength);
        System.arraycopy(original, 0, array, 0,
                Math.min(original.length, newLength));
        return array;
    }

    public static <T> T[] copyOfRange(T[] original, int from, int to) {
        int length = to - from;
        if (length < 0)
            throw new IllegalArgumentException(from + " > " + to);
        Class<?> type = original.getClass();
        @SuppressWarnings("unchecked")
        T[] array = (type == Object[].class)
                ? (T[]) new Object[length]
                : (T[]) Array.newInstance(type.getComponentType(), length);
        System.arraycopy(original, from, array, 0,
                Math.min(original.length - from, length));
        return array;
    }

}
