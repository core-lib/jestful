package org.qfox.jestful.client.auth;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by yangchangpei on 17/10/23.
 */
public class State implements Serializable {
    private static final long serialVersionUID = 6971103658173832157L;

    private final Host host;
    private final ConcurrentMap<String, Authenticator> authenticators; // <realm: option>

    public State(Host host) {
        this.host = host;
        this.authenticators = new ConcurrentHashMap<String, Authenticator>();
    }

    public Host getHost() {
        return host;
    }

    public Authenticator get(String realm) {
        if (realm == null) throw new IllegalArgumentException("realm == null");
        return authenticators.get(realm);
    }

    public Authenticator put(String realm, Authenticator authenticator) {
        if (realm == null) throw new IllegalArgumentException("realm == null");
        if (authenticator == null) throw new IllegalArgumentException("option == null");
        Authenticator old = authenticators.putIfAbsent(realm, authenticator);
        return old != null ? old : authenticator;
    }

    public boolean has(String realm) {
        if (realm == null) throw new IllegalArgumentException("realm == null");
        return authenticators.containsKey(realm);
    }

    public Set<String> realms() {
        return Collections.unmodifiableSet(authenticators.keySet());
    }

    public void clear() {
        authenticators.clear();
    }

}
