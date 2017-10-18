package org.qfox.jestful.client.retry;

import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.scheduler.Callback;

/**
 * Created by yangchangpei on 17/10/18.
 */
public class RetriablePromise implements Promise {
    private final Promise promise;

    public RetriablePromise(Promise promise) {
        this.promise = promise;
    }

    @Override
    public Object get() throws Exception {
        return null;
    }

    @Override
    public void get(Callback<Object> callback) {

    }
}
