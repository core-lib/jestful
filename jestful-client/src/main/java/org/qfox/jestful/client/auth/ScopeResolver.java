package org.qfox.jestful.client.auth;

import org.qfox.jestful.core.Action;

/**
 * Created by yangchangpei on 17/10/23.
 */
public interface ScopeResolver {

    Scope resolve(Action action);

}
