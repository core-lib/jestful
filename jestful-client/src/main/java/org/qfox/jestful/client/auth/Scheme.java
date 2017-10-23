package org.qfox.jestful.client.auth;

import org.qfox.jestful.core.Action;

/**
 * Created by Payne on 2017/10/20.
 */
public interface Scheme {

    String getName();

    void authenticate(Action action, Scope scope, Credence credence, Challenge challenge);

    boolean matches(Action action, boolean thrown, Object result, Exception exception);

    Challenge analyze(Action action, boolean thrown, Object result, Exception exception);

}
