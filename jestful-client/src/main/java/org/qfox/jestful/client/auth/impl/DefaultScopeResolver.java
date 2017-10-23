package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.Scope;
import org.qfox.jestful.client.auth.ScopeResolver;
import org.qfox.jestful.core.Action;

/**
 * Created by yangchangpei on 17/10/23.
 */
public class DefaultScopeResolver implements ScopeResolver {

    @Override
    public Scope resolve(Action action) {
        return new Scope(null, null, action.getHostname(), action.getPort());
    }

}
