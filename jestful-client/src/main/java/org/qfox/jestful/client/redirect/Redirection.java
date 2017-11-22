package org.qfox.jestful.client.redirect;

import org.qfox.jestful.core.Action;

public interface Redirection {

    String name();

    void redirect(Action action, boolean thrown, Object result, Exception exception);

    boolean matches(Action action, boolean thrown, Object result, Exception exception);

}
