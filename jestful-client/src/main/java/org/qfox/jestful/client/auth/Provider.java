package org.qfox.jestful.client.auth;

/**
 * Created by yangchangpei on 17/10/21.
 */
public interface Provider {

    void setCredence(Scope scope, Credence credence);

    Credence getCredence(Scope scope);

    void clear();

}
