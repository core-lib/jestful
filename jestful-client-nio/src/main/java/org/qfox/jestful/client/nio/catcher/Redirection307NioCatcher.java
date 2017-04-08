package org.qfox.jestful.client.nio.catcher;

import org.qfox.jestful.client.catcher.Redirection307Catcher;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.StatusException;

import java.net.URL;
import java.util.List;

/**
 * Created by yangchangpei on 17/4/7.
 */
public class Redirection307NioCatcher extends Redirection307Catcher implements NioCatcher {

    @Override
    public boolean catchable(StatusException statusException) {
        return statusException.getStatus() == 307;
    }

    @Override
    public void nioCatched(NioClient client, Action action, StatusException statusException) throws Exception {
        if (!client.isFollowRedirection()) throw statusException;

        Response response = action.getResponse();
        String location = response.getResponseHeader("Location");
        List<Parameter> parameters = action.getParameters().all(Position.BODY);
        client.invoker().setEndpoint(new URL(location))
                .setParameters(new Parameters(parameters))
                .setRestful(action.getRestful())
                .setResult(new Result(action.getResult().getBody().getType()))
                .invoke();
    }
}
