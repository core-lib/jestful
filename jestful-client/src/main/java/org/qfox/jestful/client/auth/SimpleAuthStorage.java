package org.qfox.jestful.client.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yangchangpei on 17/10/21.
 */
public class SimpleAuthStorage implements AuthStorage {
    private final Map<Host, Scheme> store = new ConcurrentHashMap<Host, Scheme>();

    @Override
    public void put(Host host, Scheme scheme) {
        store.put(host, scheme);
    }

    @Override
    public Scheme get(Host host) {
        return store.get(host);
    }

    @Override
    public void remove(Host host) {
        store.remove(host);
    }

    @Override
    public void clear() {
        store.clear();
    }
}
