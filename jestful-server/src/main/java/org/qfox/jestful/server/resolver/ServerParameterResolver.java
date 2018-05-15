package org.qfox.jestful.server.resolver;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.annotation.Variable;
import org.qfox.jestful.core.exception.BeanConfigException;
import org.qfox.jestful.server.obtainer.Acquirer;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by yangchangpei on 17/5/3.
 */
public class ServerParameterResolver implements Actor, Initialable, Destroyable, Configurable {
    private final Set<Acquirer> acquirers = new LinkedHashSet<Acquirer>();

    @Override
    public Object react(Action action) throws Exception {
        Parameters parameters = action.getParameters();
        for (Parameter parameter : parameters) {
            if (parameter.getAnnotationWith(Variable.class) != null || parameter.getValue() != null) continue;
            Object value = null;
            Iterator<Acquirer> iterator = acquirers.iterator();
            while (value == null && iterator.hasNext()) value = iterator.next().acquire(action, parameter);
            parameter.setValue(value);
        }
        return action.execute();
    }

    public void initialize(BeanContainer beanContainer) {
        acquirers.addAll(beanContainer.find(Acquirer.class).values());
    }

    @Override
    public void destroy() {
        for (Acquirer o : acquirers) if (o instanceof Destroyable) ((Destroyable) o).destroy();
    }

    @Override
    public void config(Map<String, String> arguments) throws BeanConfigException {
        for (Acquirer o : acquirers) if (o instanceof Configurable) ((Configurable) o).config(arguments);
    }

}
