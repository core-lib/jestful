package org.qfox.jestful.client.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yangchangpei on 17/10/21.
 */
public class SimpleCredenceProvider implements CredenceProvider {
    private final Map<Scope, Credence> store = new ConcurrentHashMap<Scope, Credence>();

    @Override
    public void setCredence(Scope scope, Credence credence) {
        if (scope == null) throw new IllegalArgumentException("scope must not be null");
        store.put(scope, credence);
    }

    @Override
    public Credence getCredence(Scope scope) {
        if (scope == null) throw new IllegalArgumentException("scope must not be null");
        Credence credence = store.get(scope);
        if (credence == null) {
            int degree = -1;
            Scope best = null;
            for (final Scope that : store.keySet()) {
                final int factor = scope.match(that);
                if (factor > degree) {
                    degree = factor;
                    best = that;
                }
            }
            if (best != null) credence = store.get(best);
        }
        return credence;
    }

    @Override
    public void clear() {
        store.clear();
    }

}
