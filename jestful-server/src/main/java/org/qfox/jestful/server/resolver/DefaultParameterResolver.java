package org.qfox.jestful.server.resolver;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Parameters;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangchangpei on 16/10/5.
 */
public class DefaultParameterResolver implements Actor {
    private final Map<Class<?>, Object> defaults = new HashMap<Class<?>, Object>();

    {
        defaults.put(boolean.class, false);
        defaults.put(byte.class, (byte) 0);
        defaults.put(char.class, (char) 0);
        defaults.put(short.class, (short) 0);
        defaults.put(int.class, 0);
        defaults.put(long.class, (long) 0);
        defaults.put(float.class, (float) 0);
        defaults.put(double.class, (double) 0);
    }

    public Object react(Action action) throws Exception {
        Parameters parameters = action.getParameters();

        for (Parameter parameter : parameters) {
            if (parameter.getValue() != null) {
                continue;
            }
            Class<?> klass = parameter.getKlass();
            if (defaults.containsKey(klass) == false) {
                continue;
            }
            Object value = defaults.get(klass);
            parameter.setValue(value);
        }

        return action.execute();
    }

}
