package org.qfox.jestful.client.catcher;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.StatusException;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangchangpei on 17/4/7.
 */
public class Redirection301Catcher implements Catcher {

    @Override
    public boolean catchable(StatusException statusException) {
        return statusException.getStatus() == 301;
    }

    @Override
    public Object caught(Client client, Action action, StatusException statusException) throws Exception {
        if (!client.isFollowRedirection()) throw statusException;

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
                .setResult(new Result(type))
                .invoke();
    }

}
