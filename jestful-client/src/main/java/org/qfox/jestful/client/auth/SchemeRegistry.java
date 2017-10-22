package org.qfox.jestful.client.auth;

import org.qfox.jestful.core.Action;

import java.util.Set;

public interface SchemeRegistry {

    void register(String name, Scheme scheme);

    Scheme unregister(String name);

    Scheme lookup(String name);

    Set<String> names();

    Scheme matches(Action action, boolean thrown, Object result, Exception exception);

}
