package org.qfox.jestful.client.auth;

import org.qfox.jestful.core.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleSchemeFactoryRegistry implements SchemeFactoryRegistry {
    private Map<String, SchemeFactory> map = new ConcurrentHashMap<String, SchemeFactory>();

    @Override
    public SchemeFactory lookup(String name) {
        if (name == null) return null;
        return map.get(name);
    }

    @Override
    public void register(String name, SchemeFactory schemeFactory) {
        if (name == null) throw new IllegalArgumentException("name == null");
        map.put(name, schemeFactory);
    }

    @Override
    public SchemeFactory unregister(String name) {
        if (name == null) return null;
        return map.remove(name);
    }

    @Override
    public Scheme produce(String name, Action action) {
        if (name == null) throw new IllegalArgumentException("name == null");
        SchemeFactory schemeFactory = lookup(name);
        if (schemeFactory != null) {
            return schemeFactory.produce(action);
        } else {
            throw new IllegalStateException("unsupported scheme : " + name);
        }
    }

    @Override
    public List<String> names() {
        return new ArrayList<String>(map.keySet());
    }
}
