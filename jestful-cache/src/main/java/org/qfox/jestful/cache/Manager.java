package org.qfox.jestful.cache;

import java.lang.reflect.Method;

/**
 * Created by yangchangpei on 17/9/6.
 */
public interface Manager {

    Entry get(Object object, Method method, Object... args);

    void put(String key, Period period, Object value);

}
