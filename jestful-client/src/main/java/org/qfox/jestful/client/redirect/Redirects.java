package org.qfox.jestful.client.redirect;

import org.qfox.jestful.core.Action;

public interface Redirects {

    Redirect match(Action action, boolean thrown, Object result, Exception exception);

}
