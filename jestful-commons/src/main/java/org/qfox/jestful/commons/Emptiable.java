package org.qfox.jestful.commons;

public class Emptiable<T> {
    private static final Emptiable<?> EMPTY = new Emptiable<Object>(null);

    private final T value;

    private Emptiable(T value) {
        this.value = value;
    }

    public static <T> Emptiable<T> empty() {
        return (Emptiable<T>) EMPTY;
    }

    public static <T> Emptiable<T> of(T value) {
        return new Emptiable<T>(value);
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public T get() {
        return value;
    }

}
