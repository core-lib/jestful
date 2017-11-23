package org.qfox.jestful.client.redirect;

import org.qfox.jestful.core.Action;

public interface Redirects {

    Redirect get(Action action, Redirection redirection);

    Redirect match(Action action, boolean thrown, Object result, Exception exception);

}
