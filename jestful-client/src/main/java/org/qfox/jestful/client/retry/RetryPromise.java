package org.qfox.jestful.client.retry;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.Action;

/**
 * Created by yangchangpei on 17/10/18.
 */
class RetryPromise implements Promise {
    private final Action action;
    private final Promise promise;
    private final RetryCondition retryCondition;
    private final int maxTimes;

    RetryPromise(Action action, Promise promise, RetryCondition retryCondition, int maxTimes) {
        this.action = action;
        this.promise = promise;
        this.retryCondition = retryCondition;
        this.maxTimes = maxTimes;
    }

    @Override
    public Object get() throws Exception {
        Object result = promise.get();
        if (retryCondition.matches(action)) {
            Integer times = (Integer) action.getExtra().get(this.getClass());
            if (times == null) times = 0;
            if (times >= maxTimes) return result;
            return client().invoker()
                    .setResource(action.getResource())
                    .setMapping(action.getMapping())
                    .setParameters(action.getParameters())
                    .invoke();
        } else {
            return result;
        }
    }

    @Override
    public void get(Callback<Object> callback) {

    }

    @Override
    public Client client() {
        return promise.client();
    }
}
