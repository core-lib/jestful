package org.qfox.jestful.client.redirect;

public interface Recorder {

    Redirection record(Direction direction, Redirection redirection);

    Redirection search(Direction direction);

    void remove(Direction direction);

    void clear();

}
