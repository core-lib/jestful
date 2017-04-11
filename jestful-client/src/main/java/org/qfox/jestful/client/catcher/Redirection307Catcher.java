package org.qfox.jestful.client.catcher;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.StatusException;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

/**
 * Created by yangchangpei on 17/4/7.
 */
public class Redirection307Catcher implements Catcher {

    @Override
    public boolean catchable(StatusException statusException) {
        return statusException.getStatus() == 307;
    }

    @Override
    public Object catched(Client client, Action action, StatusException statusException) throws Exception {
        if (!client.isFollowRedirection()) throw statusException;

        Response response = action.getResponse();
        String location = response.getResponseHeader("Location");
        List<Parameter> parameters = action.getParameters().all(Position.BODY);
        Type type = action.getResult().getBody().getType();
        return client.invoker().setEndpoint(new URL(location))
                .setParameters(new Parameters(parameters))
                .setRestful(action.getRestful())
                .setResult(new Result(type))
                .invoke();
    }
}
