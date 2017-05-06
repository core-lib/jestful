package org.qfox.jestful.interception;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.BeanConfigException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Payne on 2017/5/5.
 */
public class Interception implements Plugin, Interceptor, Initialable, Destroyable {
    private final Collection<Listener> listeners = new ArrayList<Listener>();

    @Override
    public void config(Map<String, String> arguments) throws BeanConfigException {
        for (Listener listener : listeners) listener.config(arguments);
    }

    @Override
    public Object react(Action action) throws Exception {
        List<Interceptor> listeners = new ArrayList<Interceptor>();
        for (Listener listener : this.listeners) if (listener.matches(action)) listeners.add(listener);
        listeners.add(this);
        Invocation invocation = new Invocation(action, listeners.toArray(new Interceptor[listeners.size()]));
        return invocation.invoke();
    }

    @Override
    public Object intercept(Invocation invocation) throws Exception {
        return invocation.execute();
    }

    @Override
    public void initialize(BeanContainer beanContainer) {
        Collection<Interceptor> interceptors = beanContainer.find(Interceptor.class).values();
        for (Interceptor interceptor : interceptors) listeners.add(new Listener(interceptor));
    }

    @Override
    public void destroy() {
        for (Listener listener : listeners) listener.destroy();
        listeners.clear();
    }

}
