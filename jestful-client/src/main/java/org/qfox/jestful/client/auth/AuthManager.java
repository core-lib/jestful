package org.qfox.jestful.client.auth;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;

/**
 * Created by Payne on 2017/10/21.
 */
public class AuthManager implements Actor {
    private Provider provider;
    private Cache cache;

    public AuthManager(Provider provider) {
        this(provider, new SimpleCache());
    }

    public AuthManager(Provider provider, Cache cache) {
        this.provider = provider;
        this.cache = cache;
    }

    @Override
    public Object react(Action action) throws Exception {
        Host host = new Host(action.getProtocol(), action.getHost(), action.getPort());
        Scheme scheme = cache != null ? cache.get(host) : null;
        if (scheme != null) {
            Scope scope = new Scope(Scope.ANY_SCHEME, Scope.ANY_REALM, action.getHost(), action.getPort());
            Credence credence = provider != null ? provider.getCredence(scope) : null;
            if (credence != null) {
            }
        }
        Promise promise = (Promise) action.execute();
        return new AuthPromise(action, promise);
    }

    private class AuthPromise implements Promise {
        private final Action action;
        private final Promise promise;

        AuthPromise(Action action, Promise promise) {
            this.action = action;
            this.promise = promise;
        }

        @Override
        public Object acquire() throws Exception {
            return null;
        }

        @Override
        public void accept(Callback<Object> callback) {

        }

        @Override
        public Client client() {
            return promise.client();
        }
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
