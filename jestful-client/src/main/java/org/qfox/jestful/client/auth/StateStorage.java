package org.qfox.jestful.client.auth;

/**
 * Created by Payne on 2017/10/20.
 */
public interface StateStorage {

    void put(Host host, State state);

    State get(Host host);

    void remove(Host host);

    void clear();

}
