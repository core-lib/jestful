package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.Scheme;
import org.qfox.jestful.client.auth.SchemeRegistry;
import org.qfox.jestful.core.Action;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MapSchemeRegistry implements SchemeRegistry {
    private final Map<String, Scheme> map = new ConcurrentHashMap<String, Scheme>();

    @Override
    public void register(String name, Scheme scheme) {
        if (name == null) throw new IllegalArgumentException("name == null");
        map.put(name.toUpperCase(), scheme);
    }

    @Override
    public Scheme unregister(String name) {
        if (name == null) throw new IllegalArgumentException("name == null");
        return map.remove(name.toUpperCase());
    }

    @Override
    public Scheme lookup(String name) {
        if (name == null) throw new IllegalArgumentException("name == null");
        return map.get(name.toUpperCase());
    }

    @Override
    public Set<String> names() {
        return Collections.unmodifiableSet(map.keySet());
    }

    @Override
    public Scheme matches(Action action, boolean thrown, Object result, Exception exception) {
        for (Scheme scheme : map.values()) if (scheme.matches(action, thrown, result, exception)) return scheme;
        return null;
    }
}
