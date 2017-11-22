package org.qfox.jestful.commons;

public interface Predication<T> {

    boolean test(T obj);

}
