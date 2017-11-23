package org.qfox.jestful.client.redirect.impl;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.exception.StatusException;

public class HTTP303Redirect extends HTTPGetRedirect {

    @Override
    public String name() {
        return "HTTP.303";
    }

    @Override
    public boolean matches(Action action, boolean thrown, Object result, Exception exception) {
        return ("http".equalsIgnoreCase(action.getProtocol()) || "https".equalsIgnoreCase(action.getProtocol()))
                && exception instanceof StatusException
                && ((StatusException) exception).getStatus() == 303;
    }

    @Override
    public boolean permanent(Action action, boolean thrown, Object result, Exception exception) {
        return false;
    }
}
