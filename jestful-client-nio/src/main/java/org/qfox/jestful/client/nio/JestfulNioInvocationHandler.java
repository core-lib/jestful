package org.qfox.jestful.client.nio;

import org.qfox.jestful.client.JestfulInvocationHandler;
import org.qfox.jestful.client.scheduler.Scheduler;
import org.qfox.jestful.core.*;

import java.lang.reflect.Type;

/**
 * Created by yangchangpei on 17/3/23.
 */
public class JestfulNioInvocationHandler<T> extends JestfulInvocationHandler<T> {

    protected JestfulNioInvocationHandler(Class<T> interfase, String protocol, String host, Integer port, String route, NioClient client) {
        super(interfase, protocol, host, port, route, client);
    }

    @Override
    protected Object doSchedule(Action action) throws Exception {
        Result result = action.getResult();
        Body body = result.getBody();
        for (Scheduler scheduler : client.getSchedulers().values()) {
            if (scheduler.supports(action)) {
                Type type = scheduler.getBodyType(client, action);
                body.setType(type);
                Object value = scheduler.schedule(client, action);
                result.setValue(value);
                return value;
            }
        }

        Type type = result.getType();
        body.setType(type);
        NioWaiting waiting = new NioWaiting() {
            @Override
            public void act() {
                synchronized (this) {
                    this.notify();
                }
            }
        };
        action.getExtra().put(NioWaiting.class, waiting);
        synchronized (waiting) {
            action.execute();
            waiting.wait();
        }

        return result.getValue();
    }

    protected Request newRequest(Action action) {
        Request request = new JestfulNioClientRequest(action, client, client.getGateway(), client.getConnTimeout(), client.getReadTimeout());
        action.getExtra().put(JestfulNioClientRequest.class, request);
        return request;
    }

    protected Response newResponse(Action action) {
        Response response = new JestfulNioClientResponse(action, client, client.getGateway());
        action.getExtra().put(JestfulNioClientResponse.class, response);
        return response;
    }

}
