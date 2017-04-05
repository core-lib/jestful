package org.qfox.jestful.client.aio;

import org.qfox.jestful.client.JestfulInvocationHandler;
import org.qfox.jestful.client.aio.connection.JestfulAioHttpClientRequest;
import org.qfox.jestful.client.aio.connection.JestfulAioHttpClientResponse;
import org.qfox.jestful.client.aio.scheduler.AioScheduler;
import org.qfox.jestful.client.scheduler.Scheduler;
import org.qfox.jestful.core.*;

import java.lang.reflect.Type;

/**
 * Created by yangchangpei on 17/3/23.
 */
public class JestfulAioInvocationHandler<T> extends JestfulInvocationHandler<T> {

    protected JestfulAioInvocationHandler(Class<T> interfase, String protocol, String host, Integer port, String route, AioClient client) {
        super(interfase, protocol, host, port, route, client);
    }

    @Override
    protected Object doSchedule(Action action) throws Exception {
        Result result = action.getResult();
        Body body = result.getBody();
        for (Scheduler scheduler : client.getSchedulers().values()) {
            if (scheduler instanceof AioScheduler && scheduler.supports(action)) {
                action.getExtra().put(Scheduler.class, scheduler);
                Type type = scheduler.getBodyType(client, action);
                body.setType(type);
                Object value = ((AioScheduler) scheduler).doMotivateSchedule(client, action);
                result.setValue(value);
                return value;
            }
        }
        throw new UnsupportedOperationException();
    }

    protected Request newRequest(Action action) throws Exception {
        AioClient aioClient = (AioClient) client;
        AioRequest request = aioClient.aioConnect(action, aioClient.getGateway(), aioClient).getRequest();
        action.getExtra().put(AioRequest.class, request);
        return request;
    }

    protected Response newResponse(Action action) throws Exception {
        AioClient aioClient = (AioClient) client;
        AioResponse response = aioClient.aioConnect(action, aioClient.getGateway(), aioClient).getResponse();
        action.getExtra().put(AioResponse.class, response);
        return response;
    }

}
