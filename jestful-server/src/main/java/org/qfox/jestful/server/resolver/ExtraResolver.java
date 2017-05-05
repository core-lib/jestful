package org.qfox.jestful.server.resolver;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.annotation.Variable;
import org.qfox.jestful.core.exception.BeanConfigException;
import org.qfox.jestful.server.obtainer.Obtainer;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by yangchangpei on 17/5/3.
 */
public class ExtraResolver implements Resolver, Initialable, Destroyable, Configurable {
    private final Set<Obtainer> obtainers = new LinkedHashSet<Obtainer>();

    @Override
    public boolean supports(Action action, Parameter parameter) {
        return parameter.getAnnotationWith(Variable.class) == null && parameter.getValue() == null;
    }

    @Override
    public void resolve(Action action, Parameter parameter) throws Exception {
        Object value = null;
        Iterator<Obtainer> iterator = obtainers.iterator();
        while (value == null && iterator.hasNext()) value = iterator.next().obtain(action, parameter);
        parameter.setValue(value);
    }

    public void initialize(BeanContainer beanContainer) {
        obtainers.addAll(beanContainer.find(Obtainer.class).values());
    }

    @Override
    public void destroy() {
        for (Obtainer o : obtainers) if (o instanceof Destroyable) ((Destroyable) o).destroy();
    }

    @Override
    public void config(Map<String, String> arguments) throws BeanConfigException {
        for (Obtainer o : obtainers) if (o instanceof Configurable) ((Configurable) o).config(arguments);
    }
}
