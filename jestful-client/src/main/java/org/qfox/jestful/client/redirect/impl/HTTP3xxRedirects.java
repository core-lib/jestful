package org.qfox.jestful.client.redirect.impl;

import org.qfox.jestful.client.redirect.Redirect;
import org.qfox.jestful.client.redirect.Redirection;
import org.qfox.jestful.client.redirect.Redirects;
import org.qfox.jestful.commons.CollectionKit;
import org.qfox.jestful.commons.Predication;
import org.qfox.jestful.core.Action;

import java.util.HashMap;
import java.util.Map;

public class HTTP3xxRedirects implements Redirects {
    private final Map<String, Redirect> map = new HashMap<String, Redirect>();

    public HTTP3xxRedirects() {
        add(new HTTP301Redirect());
        add(new HTTP302Redirect());
        add(new HTTP303Redirect());
        add(new HTTP307Redirect());
    }

    private void add(Redirect redirect) {
        map.put(redirect.name(), redirect);
    }

    @Override
    public Redirect match(Action action, Redirection redirection) {
        return map.get(redirection.getName());
    }

    @Override
    public Redirect match(final Action action, final boolean thrown, final Object result, final Exception exception) {
        return CollectionKit.any(map.values(), new Predication<Redirect>() {
            @Override
            public boolean test(Redirect redirect) {
                return redirect.matches(action, thrown, result, exception);
            }
        }).get();
    }

}
