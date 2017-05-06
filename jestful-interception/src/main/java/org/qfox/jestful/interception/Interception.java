package org.qfox.jestful.interception;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.BeanConfigException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Payne on 2017/5/5.
 */
public class Interception implements Plugin, Initialable, Destroyable {
    private final Collection<Listener> listeners = new ArrayList<Listener>();
    private final Interceptor interceptor = new Interceptor() {
        @Override
        public Object intercept(Invocation invocation) throws Exception {
            return invocation.execute();
        }
    };
    private final ConcurrentLinkedQueue<Invocation> queue = new ConcurrentLinkedQueue<Invocation>();

    @Override
    public void config(Map<String, String> arguments) throws BeanConfigException {
        for (Listener listener : listeners) listener.config(arguments);
    }

    @Override
    public Object react(Action action) throws Exception {
        Invocation invocation = queue.poll();
        if (invocation == null) invocation = new Invocation();
        try {
            List<Interceptor> interceptors = new ArrayList<Interceptor>();
            for (Listener listener : listeners) if (listener.matches(action)) interceptors.add(listener);
            interceptors.add(interceptor);
            invocation.reset(action, interceptors);
            Object value = invocation.invoke();
            action.getResult().getBody().setValue(value);
            return value;
        } catch (Exception e) {
            throw e;
        } finally {
            queue.offer(invocation);
        }
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
