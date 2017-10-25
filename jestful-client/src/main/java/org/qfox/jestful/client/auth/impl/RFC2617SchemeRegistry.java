package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.Scheme;
import org.qfox.jestful.client.auth.SchemeRegistry;

/**
 * Created by yangchangpei on 17/10/25.
 */
public final class RFC2617SchemeRegistry extends MapSchemeRegistry implements SchemeRegistry {

    public RFC2617SchemeRegistry() {
        super(new DigestScheme(), new BasicScheme());
    }

    @Override
    public void register(Scheme scheme) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Scheme unregister(String name) {
        throw new UnsupportedOperationException();
    }
}
