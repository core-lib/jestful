package org.qfox.jestful.client.auth;

import org.qfox.jestful.core.Action;

import java.util.List;

public interface SchemeFactoryRegistry {

    SchemeFactory lookup(String name);

    void register(String name, SchemeFactory schemeFactory);

    SchemeFactory unregister(String name);

    Scheme produce(String name, Action action);

    List<String> names();

}
