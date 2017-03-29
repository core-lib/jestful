package org.qfox.jestful.client.aio;

import org.qfox.jestful.core.Action;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by payne on 2017/3/29.
 */
public class WriteCompletionHandler extends AioCompletionHandler<Integer> {
    private final ByteBuffer empty = ByteBuffer.allocate(0);

    WriteCompletionHandler(AsynchronousSocketChannel channel) {
        super(channel);
    }

    @Override
    public void completed(Integer count, Action action) {
        try {
            JestfulAioClientRequest request = (JestfulAioClientRequest) action.getExtra().get(JestfulAioClientRequest.class);
            if (request.send(channel, this)) {
                AioListener listener = (AioListener) action.getExtra().get(AioListener.class);
                listener.onRequested(action);

                channel.read(empty, action, new ReadCompletionHandler(channel));
            }
        } catch (Exception e) {
            failed(e, action);
        }
    }
}
