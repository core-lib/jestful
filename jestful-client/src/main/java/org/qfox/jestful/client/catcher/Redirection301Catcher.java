package org.qfox.jestful.client.catcher;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.StatusException;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by yangchangpei on 17/4/7.
 */
public class Redirection301Catcher implements Catcher {

    @Override
    public boolean catchable(StatusException statusException) {
        return statusException.getStatus() == 301;
    }

    @Override
    public Object catched(Client client, Action action, StatusException statusException) throws Exception {
        Response response = action.getResponse();
        String location = response.getResponseHeader("Location");
        return client.invoker().setEndpoint(new URL(location))
                .setParameters(new Parameters(new ArrayList<Parameter>()))
                .setRestful(new Restful("GET", false, true, true))
                .setResult(new Result(action.getResult().getBody().getType()))
                .invoke();
    }

}
