package org.qfox.jestful.commons;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionKit {

    public static <T> T any(Collection<? extends T> collection, Predication<T>... predication) {
        if (collection == null || predication == null) throw new NullPointerException("collection and predication must not be null");
        flag: for (T obj : collection) {
            for (Predication<T> predicate : predication) if (!predicate.test(obj)) continue flag;
            return obj;
        }
        return null;
    }

    public static <T> Collection<T> all(Collection<? extends T> collection, Predication<T>... predication) {
        if (collection == null || predication == null) throw new NullPointerException("collection and predication must not be null");
        Collection<T> result = new ArrayList<T>();
        flag: for (T obj : collection) {
            for (Predication<T> predicate : predication) if (!predicate.test(obj)) continue flag;
            result.add(obj);
        }
        return result;
    }

}
