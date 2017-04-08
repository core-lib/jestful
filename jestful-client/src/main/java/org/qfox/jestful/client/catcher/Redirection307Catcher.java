package org.qfox.jestful.client.catcher;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.StatusException;

import java.net.URL;
import java.util.ArrayList;
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
        List<Parameter> parameters = new ArrayList<Parameter>();
        List<Parameter> bodies = action.getParameters().all(Position.BODY);
        parameters.addAll(bodies);
        List<Parameter> extras = action.getParameters().all((Position) null);
        parameters.addAll(extras);
        return client.invoker().setEndpoint(new URL(location))
                .setParameters(new Parameters(parameters))
                .setRestful(action.getRestful())
                .setResult(action.getResult())
                .invoke();
    }
}
