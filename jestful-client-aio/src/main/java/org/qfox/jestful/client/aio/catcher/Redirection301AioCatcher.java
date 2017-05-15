package org.qfox.jestful.client.aio.catcher;

import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.catcher.Redirection301Catcher;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.StatusException;

import java.net.URL;
import java.util.List;

/**
 * Created by yangchangpei on 17/4/7.
 */
public class Redirection301AioCatcher extends Redirection301Catcher implements AioCatcher {

    @Override
    public boolean catchable(StatusException statusException) {
        return statusException.getStatus() == 301;
    }

    @Override
    public void aioCatched(AioClient client, Action action, StatusException statusException) throws Exception {
        if (!client.isFollowRedirection()) throw statusException;

        Response response = action.getResponse();
        String location = response.getResponseHeader("Location");
        List<Parameter> parameters = action.getParameters().all(Position.UNKNOWN);
        client.invoker().setEndpoint(new URL(location))
                .setProduces(action.getProduces())
                .setConsumes(action.getConsumes())
                .setParameters(new Parameters(parameters))
                .setRestful(new Restful("GET", false, true, true))
                .setResult(action.getResult())
                .invoke();
    }

}
