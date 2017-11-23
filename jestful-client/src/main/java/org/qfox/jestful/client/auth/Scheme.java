package org.qfox.jestful.client.auth;

import org.qfox.jestful.client.auth.exception.AuthenticationException;
import org.qfox.jestful.core.Action;

/**
 * Created by Payne on 2017/10/20.
 */
public interface Scheme {

    String getName();

    void authenticate(Action action, Scope scope, Credence credence, Challenge challenge) throws AuthenticationException;

    void success(Action action, Scope scope, Credence credence, Challenge challenge) throws AuthenticationException;

    void failure(Action action, Scope scope, Credence credence, Challenge challenge) throws AuthenticationException;

    boolean matches(Action action, boolean thrown, Object result, Exception exception);

    Challenge analyze(Action action, boolean thrown, Object result, Exception exception);

}
