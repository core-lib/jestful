package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.Host;
import org.qfox.jestful.client.auth.State;
import org.qfox.jestful.client.auth.StateStorage;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by yangchangpei on 17/10/21.
 */
public class MapStateStorage implements StateStorage {
    private final ConcurrentMap<Host, State> store = new ConcurrentHashMap<Host, State>();

    private Host normalize(Host host) {
        String protocol = host.getProtocol().toLowerCase();
        String name = host.getName().toLowerCase();
        int port = host.getPort();
        return new Host(protocol, name, port);
    }

    @Override
    public State put(Host host, State state) {
        if (host == null) throw new IllegalArgumentException("host == null");
        if (state == null) throw new IllegalArgumentException("state == null");
        State old = store.putIfAbsent(normalize(host), state);
        return old != null ? old : state;
    }

    @Override
    public State get(Host host) {
        if (host == null) throw new IllegalArgumentException("host == null");
        return store.get(normalize(host));
    }

    @Override
    public void remove(Host host) {
        if (host == null) throw new IllegalArgumentException("host == null");
        store.remove(normalize(host));
    }

    @Override
    public void clear() {
        store.clear();
    }

    @Override
    public Set<Host> hosts() {
        return Collections.unmodifiableSet(store.keySet());
    }
}
