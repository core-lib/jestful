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
    private final ConcurrentMap<String, Authentication> authentications; // <realm: authentication>
    private Authentication current;

    public State(Host host) {
        this.host = host;
        this.authentications = new ConcurrentHashMap<String, Authentication>();
    }

    public Host getHost() {
        return host;
    }

    public Authentication get(String realm) {
        if (realm == null) throw new IllegalArgumentException("realm == null");
        return authentications.get(realm);
    }

    public Authentication put(String realm, Authentication authentication) {
        if (realm == null) throw new IllegalArgumentException("realm == null");
        if (authentication == null) throw new IllegalArgumentException("option == null");
        Authentication old = authentications.putIfAbsent(realm, authentication);
        return old != null ? old : authentication;
    }

    public boolean has(String realm) {
        if (realm == null) throw new IllegalArgumentException("realm == null");
        return authentications.containsKey(realm);
    }

    public Set<String> realms() {
        return Collections.unmodifiableSet(authentications.keySet());
    }

    public void clear() {
        authentications.clear();
    }

    public Authentication getCurrent() {
        return current;
    }

    public void setCurrent(Authentication current) {
        this.current = current;
    }
}
