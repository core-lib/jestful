package org.qfox.jestful.client.redirect;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.Action;

public interface Redirect {

    String name();

    boolean matches(Action action, boolean thrown, Object result, Exception exception);

    boolean permanent(Action action, boolean thrown, Object result, Exception exception);

    Client.Invoker<?> construct(Client client, Action action, boolean thrown, Object result, Exception exception) throws Exception;

    Client.Invoker<?> construct(Client client, Action action, Redirection redirection) throws Exception;

}
