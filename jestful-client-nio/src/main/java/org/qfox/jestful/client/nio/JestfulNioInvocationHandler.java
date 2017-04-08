package org.qfox.jestful.client.nio;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.JestfulInvocationHandler;
import org.qfox.jestful.core.Actor;

/**
 * Created by yangchangpei on 17/3/23.
 */
public class JestfulNioInvocationHandler<T> extends JestfulInvocationHandler<T> {

    protected JestfulNioInvocationHandler(Class<T> interfase, String protocol, String host, Integer port, String route, NioClient client) {
        super(interfase, protocol, host, port, route, client);
    }

    public JestfulNioInvocationHandler(Class<T> interfase, String protocol, String host, Integer port, String route, Client client, Actor[] forePlugins, Actor[] backPlugins) {
        super(interfase, protocol, host, port, route, client, forePlugins, backPlugins);
    }

}
