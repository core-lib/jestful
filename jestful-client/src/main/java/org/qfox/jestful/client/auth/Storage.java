package org.qfox.jestful.client.auth;

/**
 * Created by Payne on 2017/10/20.
 */
public interface Storage {

    void put(Host host, Scheme scheme);

    Scheme get(Host host);

    void remove(Host host);

    void clear();

}
