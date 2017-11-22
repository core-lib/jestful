package org.qfox.jestful.client.redirect;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by yangchangpei on 17/10/31.
 */
public class Redirector implements Actor {
    private int maxCount;
    private Set<Redirection> redirections = new LinkedHashSet<Redirection>();

    @Override
    public Object react(Action action) throws Exception {
        return null;
    }

    private class RedirectPromise implements Promise {
        private final Action action;
        private final Promise promise;

        RedirectPromise(Action action, Promise promise) {
            this.action = action;
            this.promise = promise;
        }

        @Override
        public Object acquire() throws Exception {
            boolean thrown;
            Object result = null;
            Exception exception = null;
            try {
                result = promise.acquire();
            } catch (Exception e) {
                exception = e;
            } finally {
                thrown = exception != null;
            }



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

}
