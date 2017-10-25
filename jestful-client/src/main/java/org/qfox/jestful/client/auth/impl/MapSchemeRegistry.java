package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.Scheme;
import org.qfox.jestful.client.auth.SchemeRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MapSchemeRegistry implements SchemeRegistry {
    private final Map<String, Scheme> map = new ConcurrentHashMap<String, Scheme>();

    public MapSchemeRegistry() {
    }

    public MapSchemeRegistry(Scheme... schemes) {
        for (Scheme scheme : schemes) {
            if (scheme == null) throw new IllegalArgumentException("scheme == null");
            map.put(scheme.getName().toUpperCase(), scheme);
        }
    }

    @Override
    public void register(Scheme scheme) {
        if (scheme == null) throw new IllegalArgumentException("scheme == null");
        map.put(scheme.getName().toUpperCase(), scheme);
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

}
