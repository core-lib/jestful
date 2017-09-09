package org.qfox.jestful.cache.implementation;

import org.qfox.jestful.cache.Entry;
import org.qfox.jestful.cache.Manager;
import org.qfox.jestful.cache.Period;

import java.lang.reflect.Method;

/**
 * Created by yangchangpei on 17/9/9.
 */
public class DefaultManager implements Manager {

    @Override
    public Entry get(Object object, Method method, Object... args) {
        return null;
    }

    @Override
    public void put(String key, Period period, Object value) {

    }
}
