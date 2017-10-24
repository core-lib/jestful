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
    private final ConcurrentMap<Realm, Authentication> authentications; // <realm: authentication>
    private Authentication target;
    private Authentication proxy;

    public State(Host host) {
        this.host = host;
        this.authentications = new ConcurrentHashMap<Realm, Authentication>();
    }

    public Host getHost() {
        return host;
    }

    public Authentication get(Realm realm) {
        if (realm == null) throw new IllegalArgumentException("realm == null");
        return authentications.get(realm);
    }

    public Authentication put(Realm realm, Authentication authentication) {
        if (realm == null) throw new IllegalArgumentException("realm == null");
        if (authentication == null) throw new IllegalArgumentException("option == null");
        Authentication old = authentications.putIfAbsent(realm, authentication);
        return old != null ? old : authentication;
    }

    public boolean has(Realm realm) {
        if (realm == null) throw new IllegalArgumentException("realm == null");
        return authentications.containsKey(realm);
    }

    public Set<Realm> realms() {
        return Collections.unmodifiableSet(authentications.keySet());
    }

    public void clear() {
        authentications.clear();
    }

    public Authentication getTarget() {
        return target;
    }

    public void setTarget(Authentication target) {
        this.target = target;
    }

    public Authentication getProxy() {
        return proxy;
    }

    public void setProxy(Authentication proxy) {
        this.proxy = proxy;
    }
}
