package org.qfox.jestful.client.redirect.impl;

import org.qfox.jestful.client.redirect.Direction;
import org.qfox.jestful.client.redirect.Recorder;
import org.qfox.jestful.client.redirect.Redirection;

public class LazyManRecorder implements Recorder {

    @Override
    public Redirection record(Direction direction, Redirection redirection) {
        return null;
    }

    @Override
    public Redirection search(Direction direction) {
        return null;
    }

    @Override
    public void remove(Direction direction) {

    }

    @Override
    public void clear() {

    }
}
