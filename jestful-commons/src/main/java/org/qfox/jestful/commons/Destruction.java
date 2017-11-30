package org.qfox.jestful.commons;

import java.io.Closeable;
import java.io.IOException;

public class Destruction implements Closeable, Destructible {
    private final Destructible destructible;

    public Destruction(Destructible destructible) {
        this.destructible = destructible;
    }

    @Override
    public void close() throws IOException {
        if (destructible != null) destructible.destroy();
    }

    @Override
    public void destroy() {
        if (destructible != null) destructible.destroy();
    }
}
