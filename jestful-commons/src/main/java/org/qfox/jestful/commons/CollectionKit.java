package org.qfox.jestful.commons;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionKit {

    public static <T> Equivocal<T> any(Collection<T> collection, Predication<T> predication) {
        if (collection == null || predication == null) throw new NullPointerException("collection and predication must not be null");
        for (T obj : collection) if (predication.test(obj)) return Equivocal.of(obj);
        return Equivocal.empty();
    }

    public static <T> Collection<T> all(Collection<T> collection, Predication<T> predication) {
        if (collection == null || predication == null) throw new NullPointerException("collection and predication must not be null");
        Collection<T> result = new ArrayList<T>();
        for (T obj : collection) if (predication.test(obj)) result.add(obj);
        return result;
    }

}
