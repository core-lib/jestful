package org.qfox.jestful.client.redirect;

public interface Recorder {

    Redirection put(Direction direction, Redirection redirection);

    Redirection get(Direction direction);

    void remove(Direction direction);

    void clear();

}
