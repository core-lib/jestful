package org.qfox.jestful.client.redirect.impl;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.exception.StatusException;

public class HTTP302Redirect extends HTTP301Redirect {

    @Override
    public boolean matches(Action action, boolean thrown, Object result, Exception exception) {
        return ("http".equalsIgnoreCase(action.getProtocol()) || "https".equalsIgnoreCase(action.getProtocol()))
                && exception instanceof StatusException
                && ((StatusException) exception).getStatus() == 302;
    }

    @Override
    public boolean permanent(Action action, boolean thrown, Object result, Exception exception) {
        return false;
    }
}
