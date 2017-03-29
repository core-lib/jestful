package org.qfox.jestful.client.aio;

import org.qfox.jestful.core.Action;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by yangchangpei on 17/3/29.
 */
public class ConnectCompletionHandler extends AioCompletionHandler<Void> {
    private final ByteBuffer empty = ByteBuffer.allocate(0);

    ConnectCompletionHandler(AsynchronousSocketChannel channel) {
        super(channel);
    }

    @Override
    public void completed(Void result, Action action) {
        try {
            AioListener listener = (AioListener) action.getExtra().get(AioListener.class);
            listener.onConnected(action);

            channel.write(empty, action, new WriteCompletionHandler(channel));
        } catch (Exception e) {
            failed(e, action);
        }
    }

}
