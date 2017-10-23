package org.qfox.jestful.client.auth;

import java.util.Set;

/**
 * Created by Payne on 2017/10/20.
 */
public interface StateStorage {

    /**
     * put {@param state} associated with {@param host}
     * if there is already exists ignore it and return the old value
     * else put it and return this {@param state}
     * no matter which state it will never return {@code null}
     *
     * @param host  the host
     * @param state the state associated with it's host
     * @return old state if the host already associated a state or the new state but never null
     */
    State put(Host host, State state);

    State get(Host host);

    void remove(Host host);

    void clear();

    Set<Host> hosts();

}
