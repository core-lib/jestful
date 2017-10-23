package org.qfox.jestful.client.aio;

import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by yangchangpei on 17/4/17.
 */
public class PrepareCompletionHandler extends AioCompletionHandler<Void> implements Runnable {
    private final Action action;

    PrepareCompletionHandler(AioClient client, AsynchronousSocketChannel channel, Action action) {
        super(client, channel, 0);
        this.action = action;
    }

    @Override
    public void onCompleted(Void result, Action action) throws Exception {
        String protocol = action.getProtocol();
        String host = action.getHostname();
        Integer port = action.getPort();
        port = port != null && port >= 0 ? port : "https".equalsIgnoreCase(protocol) ? 443 : "http".equalsIgnoreCase(protocol) ? 80 : 0;
        Gateway gateway = client.getGateway();
        SocketAddress address = gateway != null && gateway.isProxy() ? gateway.toSocketAddress() : new InetSocketAddress(host, port);
        (gateway != null ? gateway : Gateway.NULL).onConnected(action);
        channel.connect(address, action, new ConnectCompletionHandler(client, channel));
    }

    @Override
    public void run() {
        completed(null, action);
    }

}
