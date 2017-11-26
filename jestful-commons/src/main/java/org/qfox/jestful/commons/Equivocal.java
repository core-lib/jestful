package org.qfox.jestful.commons;

public class Equivocal<T> {
    private static final Equivocal<?> EMPTY = new Equivocal<Object>(null);

    private final T value;

    private Equivocal(T value) {
        this.value = value;
    }

    public static <T> Equivocal<T> empty() {
        return (Equivocal<T>) EMPTY;
    }

    public static <T> Equivocal<T> of(T value) {
        return new Equivocal<T>(value);
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public T get() {
        return value;
    }

}
