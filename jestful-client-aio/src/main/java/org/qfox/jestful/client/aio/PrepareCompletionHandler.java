package org.qfox.jestful.client.aio;

import org.qfox.jestful.client.aio.connection.AioConnection;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

/**
 * Created by yangchangpei on 17/4/17.
 */
public class PrepareCompletionHandler extends AioCompletionHandler<Void> implements Runnable {
    private final Action action;

    PrepareCompletionHandler(AioClient client, AioConnection connection, Action action) {
        super(client, connection, 0);
        this.action = action;
    }

    @Override
    protected void onCompleted(Void result, Action action) throws Exception {
        Gateway gateway = client.getGateway();
        (gateway != null ? gateway : Gateway.NULL).onConnected(action);
        if (connection.isConnected()) {
            AioEventListener listener = (AioEventListener) action.getExtra().get(AioEventListener.class);
            listener.onConnected(action);

            new WriteCompletionHandler(client, connection, connection.getWriteTimeout()).completed(-1, action);
            new ReadCompletionHandler(client, connection, connection.getReadTimeout()).completed(-1, action);
        } else {
            AioOptions options = client.getOptions();
            connection.config(options);
            connection.connect(action, new ConnectCompletionHandler(client, this.connection));
        }
    }

    @Override
    public void run() {
        completed(null, action);
    }

}
