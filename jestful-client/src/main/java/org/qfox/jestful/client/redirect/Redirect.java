package org.qfox.jestful.client.redirect;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.Action;

public interface Redirect {

    Client.Invoker<?> construct(Client client, Action action, boolean thrown, Object result, Exception exception) throws Exception;

    boolean matches(Action action, boolean thrown, Object result, Exception exception);

}
