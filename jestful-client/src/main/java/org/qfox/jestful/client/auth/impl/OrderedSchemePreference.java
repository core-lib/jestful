package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.Scheme;
import org.qfox.jestful.client.auth.SchemePreference;
import org.qfox.jestful.client.auth.SchemeRegistry;
import org.qfox.jestful.core.Action;

/**
 * Created by yangchangpei on 17/10/25.
 */
public class OrderedSchemePreference implements SchemePreference {
    private final String[] names;

    public OrderedSchemePreference(String... names) {
        this.names = names;
    }

    @Override
    public Scheme matches(SchemeRegistry schemeRegistry, Action action, boolean thrown, Object result, Exception exception) {
        for (String name : names) {
            if (name == null) continue;
            Scheme scheme = schemeRegistry.lookup(name);
            if (scheme == null) continue;
            boolean matched = scheme.matches(action, thrown, result, exception);
            if (matched) return scheme;
        }
        return null;
    }
}
