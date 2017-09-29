package org.qfox.jestful.client.aio;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.JestfulInvocationHandler;
import org.qfox.jestful.core.Actor;

import java.util.List;

/**
 * Created by yangchangpei on 17/3/23.
 */
public class JestfulAioInvocationHandler<T> extends JestfulInvocationHandler<T> {

    protected JestfulAioInvocationHandler(Class<T> interfase, String protocol, String host, Integer port, String route, AioClient client) {
        super(interfase, protocol, host, port, route, client);
    }

    public JestfulAioInvocationHandler(Class<T> interfase, String protocol, String host, Integer port, String route, Client client, List<Actor> forePlugins, List<Actor> backPlugins) {
        super(interfase, protocol, host, port, route, client, forePlugins, backPlugins);
    }

}
