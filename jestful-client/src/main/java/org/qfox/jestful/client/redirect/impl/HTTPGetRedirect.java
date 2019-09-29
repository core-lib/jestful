package org.qfox.jestful.client.redirect.impl;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.redirect.Redirect;
import org.qfox.jestful.client.redirect.Redirection;
import org.qfox.jestful.core.*;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class HTTPGetRedirect implements Redirect {

    @Override
    public Client.Invoker<?> construct(Client client, Action action, boolean thrown, Object result, Exception exception) throws Exception {
        Response response = action.getResponse();
        String location = response.getResponseHeader("Location");
        return construct(client, action, new Redirection(name(), "GET", location));
    }

    @Override
    public Client.Invoker<?> construct(Client client, Action action, Redirection redirection) throws Exception {
        List<Parameter> parameters = new ArrayList<Parameter>();
        Type type = action.getResult().getBody().getType();
        return client.invoker()
                .setEndpoint(new URL(redirection.getURL()))
                .setProduces(action.getProduces())
                .setConsumes(action.getConsumes())
                .setParameters(new Parameters(parameters))
                .setRestful(Restful.GET)
                .setResult(new Result(type));
    }
}
