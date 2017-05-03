package org.qfox.jestful.server.resolver;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangchangpei on 17/5/3.
 */
public class DefaultResolver implements Resolver {
    private final Map<Class<?>, Object> defaults = new HashMap<Class<?>, Object>();

    public DefaultResolver() {
        defaults.put(boolean.class, false);
        defaults.put(byte.class, (byte) 0);
        defaults.put(char.class, (char) 0);
        defaults.put(short.class, (short) 0);
        defaults.put(int.class, 0);
        defaults.put(long.class, (long) 0);
        defaults.put(float.class, (float) 0);
        defaults.put(double.class, (double) 0);
    }

    @Override
    public boolean supports(Action action, Parameter parameter) {
        return parameter.getValue() == null;
    }

    @Override
    public void resolve(Action action, Parameter parameter) throws Exception {
        Class<?> klass = parameter.getKlass();
        if (!defaults.containsKey(klass)) return;
        Object value = defaults.get(klass);
        parameter.setValue(value);
    }
}
