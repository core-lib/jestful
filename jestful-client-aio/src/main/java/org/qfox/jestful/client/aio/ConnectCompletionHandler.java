package org.qfox.jestful.client.aio;

import org.qfox.jestful.client.aio.connection.AioConnection;
import org.qfox.jestful.core.Action;

/**
 * Created by yangchangpei on 17/3/29.
 */
public class ConnectCompletionHandler extends AioCompletionHandler<Void> {

    ConnectCompletionHandler(AioClient client, AioConnection connection) {
        super(client, connection, 0);
    }

    @Override
    protected void onCompleted(Void result, Action action) throws Exception {
        AioEventListener listener = (AioEventListener) action.getExtra().get(AioEventListener.class);
        listener.onConnected(action);

        new WriteCompletionHandler(client, connection, connection.getWriteTimeout()).completed(-1, action);
        new ReadCompletionHandler(client, connection, connection.getReadTimeout()).completed(-1, action);
    }

}
