package org.qfox.jestful.client.aio;

import org.qfox.jestful.core.Action;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by yangchangpei on 17/3/29.
 */
public class ConnectCompletionHandler extends AioCompletionHandler<Void> {

    ConnectCompletionHandler(AsynchronousSocketChannel channel) {
        super(channel, 0);
    }

    @Override
    public void onCompleted(Void result, Action action) throws Exception {
        AioRequest request = (AioRequest) action.getExtra().get(AioRequest.class);
        AioEventListener listener = (AioEventListener) action.getExtra().get(AioEventListener.class);
        listener.onConnected(action);

        new WriteCompletionHandler(channel, request.getWriteTimeout()).completed(-1, action);
        new ReadCompletionHandler(channel, request.getReadTimeout()).completed(-1, action);
    }

}
