package org.qfox.jestful.cache.implementation;

import org.qfox.jestful.cache.*;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;

import java.lang.reflect.Method;

/**
 * Created by yangchangpei on 17/9/9.
 */
public class DefaultManager implements Manager, Initialable {
    private Generator generator;

    @Override
    public Entry get(Object object, Method method, Object... args) {
        Parameter[] parameters = new Parameter[args.length];
        for (int i = 0 ; i < args.length ; i ++) parameters[i] = new Parameter(method, i, args[i]);
        return null;
    }

    @Override
    public void put(String key, Period period, Object value) {

    }

    @Override
    public void initialize(BeanContainer beanContainer) {
        generator = beanContainer.get(Generator.class);
    }
}
