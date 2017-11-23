package org.qfox.jestful.client.redirect.impl;

import org.qfox.jestful.client.redirect.Direction;
import org.qfox.jestful.client.redirect.Recorder;
import org.qfox.jestful.client.redirect.Redirection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class HashMapRecorder implements Recorder {

    private final ConcurrentMap<Direction, Redirection> store = new ConcurrentHashMap<Direction, Redirection>();

    @Override
    public Redirection put(Direction direction, Redirection redirection) {
        if (direction == null) throw new IllegalArgumentException("direction == null");
        if (redirection == null) throw new IllegalArgumentException("redirection == null");
        return store.put(direction, redirection);
    }

    @Override
    public Redirection get(Direction direction) {
        if (direction == null) throw new IllegalArgumentException("direction == null");
        return store.get(direction);
    }

    @Override
    public void remove(Direction direction) {
        if (direction == null) throw new IllegalArgumentException("direction == null");
        store.remove(direction);
    }

    @Override
    public void clear() {
        store.clear();
    }
}
