package org.qfox.jestful.client.auth;

import java.util.Set;

public interface SchemeRegistry {

    void register(Scheme scheme);

    Scheme unregister(String name);

    Scheme lookup(String name);

    Set<String> names();

}
