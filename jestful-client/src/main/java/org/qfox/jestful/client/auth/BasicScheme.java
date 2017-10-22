package org.qfox.jestful.client.auth;

import org.qfox.jestful.commons.Base64;
import org.qfox.jestful.core.Action;

public class BasicScheme implements Scheme {

    @Override
    public String getName() {
        return "BASIC";
    }

    @Override
    public void cope(Challenge challenge) {

    }

    @Override
    public void authenticate(Action action, Credence credence) {
        String username = credence.getPrincipal().getName();
        String password = credence.getPassword();
        String authorization = "Basic " + Base64.encode(username + ":" + password);
        action.getRequest().setRequestHeader("Authorization", authorization);
    }

}
