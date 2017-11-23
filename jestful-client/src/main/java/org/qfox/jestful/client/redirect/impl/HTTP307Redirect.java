package org.qfox.jestful.client.redirect.impl;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.redirect.Redirect;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.StatusException;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

public class HTTP307Redirect implements Redirect {

    @Override
    public boolean matches(Action action, boolean thrown, Object result, Exception exception) {
        return ("http".equalsIgnoreCase(action.getProtocol()) || "https".equalsIgnoreCase(action.getProtocol()))
                && exception instanceof StatusException
                && ((StatusException) exception).getStatus() == 307;
    }

    @Override
    public boolean permanent(Action action, boolean thrown, Object result, Exception exception) {
        return false;
    }

    @Override
    public Client.Invoker<?> construct(Client client, Action action, boolean thrown, Object result, Exception exception) throws Exception {
        Response response = action.getResponse();
        String location = response.getResponseHeader("Location");
        List<Parameter> parameters = action.getParameters().all(Position.BODY);
        Type type = action.getResult().getBody().getType();
        return client.invoker()
                .setEndpoint(new URL(location))
                .setProduces(action.getProduces())
                .setConsumes(action.getConsumes())
                .setParameters(new Parameters(parameters))
                .setRestful(action.getRestful())
                .setResult(new Result(type));
    }
}
