package org.qfox.jestful.client.auth;

import org.qfox.jestful.core.Action;

public class BasicSchemeFactory implements SchemeFactory {

    @Override
    public Scheme produce(Action action) {
        return new BasicScheme();
    }

}
