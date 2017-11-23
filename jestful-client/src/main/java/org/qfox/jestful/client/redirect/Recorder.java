package org.qfox.jestful.client.redirect;

public interface Recorder {

    Redirection put(Redirection source, Redirection target);

    Redirection get(Redirection source);

    void remove(Redirection source);

    void clear();

}
