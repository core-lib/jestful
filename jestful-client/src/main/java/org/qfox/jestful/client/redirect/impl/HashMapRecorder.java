package org.qfox.jestful.client.redirect.impl;

import org.qfox.jestful.client.redirect.Recorder;
import org.qfox.jestful.client.redirect.Redirection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class HashMapRecorder implements Recorder {

    private final ConcurrentMap<Redirection, Redirection> store = new ConcurrentHashMap<Redirection, Redirection>();

    @Override
    public Redirection put(Redirection source, Redirection target) {
        if (source == null) throw new IllegalArgumentException("source == null");
        if (target == null) throw new IllegalArgumentException("target == null");
        return store.put(source, target);
    }

    @Override
    public Redirection get(Redirection source) {
        if (source == null) throw new IllegalArgumentException("source == null");
        return store.get(source);
    }

    @Override
    public void remove(Redirection source) {
        if (source == null) throw new IllegalArgumentException("source == null");
        store.remove(source);
    }

    @Override
    public void clear() {
        store.clear();
    }
}
