package org.qfox.jestful.client.redirect.impl;

import org.qfox.jestful.client.redirect.Redirect;
import org.qfox.jestful.client.redirect.Redirects;
import org.qfox.jestful.commons.CollectionKit;
import org.qfox.jestful.commons.Predication;
import org.qfox.jestful.core.Action;

import java.util.Arrays;
import java.util.Collection;

public class HTTP3xxRedirects implements Redirects {
    private final Collection<Redirect> defaults = Arrays.asList(
            new HTTP301Redirect(),
            new HTTP302Redirect(),
            new HTTP303Redirect(),
            new HTTP307Redirect()
    );

    @Override
    public Redirect match(final Action action, final boolean thrown, final Object result, final Exception exception) {
        return CollectionKit.any(defaults, new Predication<Redirect>() {
            @Override
            public boolean test(Redirect redirect) {
                return redirect.matches(action, thrown, result, exception);
            }
        }).get();
    }

}
