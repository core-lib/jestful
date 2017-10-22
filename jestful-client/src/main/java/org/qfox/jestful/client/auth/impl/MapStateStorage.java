package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.Host;
import org.qfox.jestful.client.auth.State;
import org.qfox.jestful.client.auth.StateStorage;
import org.qfox.jestful.client.exception.UnsupportedProtocolException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yangchangpei on 17/10/21.
 */
public class MapStateStorage implements StateStorage {
    private final Map<Host, State> store = new ConcurrentHashMap<Host, State>();

    private Host normalize(Host host) {
        String protocol = host.getProtocol().toLowerCase();
        String name = host.getName().toLowerCase();
        int port = host.getPort();
        if (port > 0) {
            return new Host(protocol, name, port);
        } else if ("https".equals(protocol)) {
            port = 443;
        } else if ("http".equals(protocol)) {
            port = 80;
        } else {
            throw new UnsupportedProtocolException(host.getProtocol());
        }
        return new Host(protocol, name, port);
    }

    @Override
    public void put(Host host, State state) {
        if (host == null) throw new IllegalArgumentException("host == null");
        store.put(normalize(host), state);
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
}
