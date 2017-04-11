package org.qfox.jestful.client.aio.catcher;

import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.catcher.Redirection307Catcher;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.StatusException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangchangpei on 17/4/7.
 */
public class Redirection307AioCatcher extends Redirection307Catcher implements AioCatcher {

    @Override
    public boolean catchable(StatusException statusException) {
        return statusException.getStatus() == 307;
    }

    @Override
    public void aioCatched(AioClient client, Action action, StatusException statusException) throws Exception {
        if (!client.isFollowRedirection()) throw statusException;

        Response response = action.getResponse();
        String location = response.getResponseHeader("Location");
        List<Parameter> params = new ArrayList<>();
        List<Parameter> bodies = action.getParameters().all(Position.BODY);
        List<Parameter> extras = action.getParameters().all((Position) null);
        params.addAll(bodies);
        params.addAll(extras);
        client.invoker().setEndpoint(new URL(location))
                .setProduces(action.getProduces())
                .setConsumes(action.getConsumes())
                .setParameters(new Parameters(params))
                .setRestful(action.getRestful())
                .setResult(action.getResult())
                .invoke();
    }
}
