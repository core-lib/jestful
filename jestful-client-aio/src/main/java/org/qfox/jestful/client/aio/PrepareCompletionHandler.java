package org.qfox.jestful.client.aio;

import org.qfox.jestful.client.aio.connection.AioConnection;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

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
        Gateway gateway = client.getGateway();
        (gateway != null ? gateway : Gateway.NULL).onConnected(action);
        AioOptions options = client.getOptions();
        options.config(channel);
        AioConnection connection = (AioConnection) action.getExtra().get(AioConnection.class);
        SocketAddress address = connection.getAddress();
        channel.connect(address, action, new ConnectCompletionHandler(client, channel));
    }

    @Override
    public void run() {
        completed(null, action);
    }

}
