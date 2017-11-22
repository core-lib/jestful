package org.qfox.jestful.client.redirect;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.exception.StatusException;

public class Redirect302 extends Redirect301 {

    @Override
    public boolean matches(Action action, boolean thrown, Object result, Exception exception) {
        return exception instanceof StatusException && ((StatusException) exception).getStatus() == 302;
    }
}
