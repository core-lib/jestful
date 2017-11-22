package org.qfox.jestful.client.redirect;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.StatusException;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Redirect301 implements Redirect {

    @Override
    public Client.Invoker<?> construct(Client client, Action action, boolean thrown, Object result, Exception exception)  throws Exception{
        Response response = action.getResponse();
        String location = response.getResponseHeader("Location");
        List<Parameter> parameters = new ArrayList<Parameter>();
        Type type = action.getResult().getBody().getType();
        return client.invoker()
                .setEndpoint(new URL(location))
                .setProduces(action.getProduces())
                .setConsumes(action.getConsumes())
                .setParameters(new Parameters(parameters))
                .setRestful(new Restful("GET", false, true, true))
                .setResult(new Result(type));
    }

    @Override
    public boolean matches(Action action, boolean thrown, Object result, Exception exception) {
        return exception instanceof StatusException && ((StatusException) exception).getStatus() == 301;
    }
}
