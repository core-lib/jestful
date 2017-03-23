package org.qfox.jestful.client.nio;

import org.qfox.jestful.client.JestfulInvocationHandler;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;

/**
 * Created by yangchangpei on 17/3/23.
 */
public class JestfulNioInvocationHandler<T> extends JestfulInvocationHandler<T> {

    protected JestfulNioInvocationHandler(Class<T> interfase, String protocol, String host, Integer port, String route, NioClient client) {
        super(interfase, protocol, host, port, route, client);
    }

    protected Request newRequest(Action action) {
        return new JestfulNioClientRequest(action, client, client.getGateway(), client.getConnTimeout(), client.getReadTimeout());
    }

    protected Response newResponse(Action action) {
        return new JestfulNioClientResponse(action, client, client.getGateway());
    }

}
