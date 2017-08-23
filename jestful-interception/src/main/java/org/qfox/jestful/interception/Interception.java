package org.qfox.jestful.interception;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.BeanConfigException;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Payne on 2017/5/5.
 */
public class Interception implements Plugin, Initialable, Destroyable {
    private final List<Listener> listeners = new ArrayList<Listener>();
    private final Queue<Invocation> queue = new ConcurrentLinkedQueue<Invocation>();

    @Override
    public void config(Map<String, String> arguments) throws BeanConfigException {
        for (Listener listener : listeners) listener.config(arguments);
    }

    @Override
    public Object react(Action action) throws Exception {
        Invocation invocation = queue.poll();
        if (invocation == null) invocation = new Invocation();
        try {
            invocation.reset(action);
            for (Listener listener : listeners) if (listener.matches(action)) invocation.accept(listener);
            Object value = invocation.invoke();
            action.getResult().setValue(value);
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
        Collections.sort(listeners, Sequential.COMPARATOR);
    }

    @Override
    public void destroy() {
        for (Listener listener : listeners) listener.destroy();
        listeners.clear();
    }

}
