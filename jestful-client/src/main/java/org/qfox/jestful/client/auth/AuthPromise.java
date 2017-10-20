package org.qfox.jestful.client.auth;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.exception.UnexpectedStatusException;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.Action;

/**
 * Created by Payne on 2017/10/21.
 */
class AuthPromise implements Promise {
    private final Action action;
    private final Promise promise;

    AuthPromise(Action action, Promise promise) {
        this.action = action;
        this.promise = promise;
    }

    @Override
    public Object acquire() throws Exception {
        try {
            return promise.acquire();
        } catch (UnexpectedStatusException e) {
            switch (e.getStatus()) {
                case 401: // Target Challenge
                    String authenticate = action.getResponse().getResponseHeader("WWW-Authenticate");

                    break;
                case 407: // Proxy Challenge

                    break;
                default:
                    throw e;
            }
            throw e;
        }
    }

    @Override
    public void accept(Callback<Object> callback) {

    }

    @Override
    public Client client() {
        return promise.client();
    }
}
