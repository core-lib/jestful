package org.qfox.jestful.client.auth;

import org.qfox.jestful.core.Action;

/**
 * Created by Payne on 2017/10/20.
 */
public interface Scheme {

    String getName();

    void cope(Challenge challenge);

    void authenticate(Action action, Credence credence);

}
