package org.qfox.jestful.client.nio;

import org.qfox.jestful.client.JestfulInvocationHandler;
import org.qfox.jestful.client.nio.scheduler.NioScheduler;
import org.qfox.jestful.client.scheduler.Scheduler;
import org.qfox.jestful.core.*;

import java.lang.reflect.Type;

/**
 * Created by yangchangpei on 17/3/23.
 */
public class JestfulNioInvocationHandler<T> extends JestfulInvocationHandler<T> {
    private NioClient nioClient;

    protected JestfulNioInvocationHandler(Class<T> interfase, String protocol, String host, Integer port, String route, NioClient client) {
        super(interfase, protocol, host, port, route, client);
        this.nioClient = client;
    }

    @Override
    protected Object doSchedule(Action action) throws Exception {
        Result result = action.getResult();
        Body body = result.getBody();
        for (Scheduler scheduler : client.getSchedulers().values()) {
            if (scheduler instanceof NioScheduler && scheduler.supports(action)) {
                action.getExtra().put(Scheduler.class, scheduler);
                Type type = scheduler.getBodyType(client, action);
                body.setType(type);
                Object value = ((NioScheduler) scheduler).doMotivateSchedule(client, action);
                result.setValue(value);
                return value;
            }
        }
        throw new UnsupportedOperationException();
    }

    protected Request newRequest(Action action) throws Exception {
        NioRequest request = nioClient.nioConnect(action, nioClient.getGateway(), nioClient).getRequest();
        action.getExtra().put(NioRequest.class, request);
        return request;
    }

    protected Response newResponse(Action action) throws Exception {
        NioResponse response = nioClient.nioConnect(action, nioClient.getGateway(), nioClient).getResponse();
        action.getExtra().put(NioResponse.class, response);
        return response;
    }

}
