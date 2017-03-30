package org.qfox.jestful.client.aio;

import org.qfox.jestful.core.Action;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by payne on 2017/3/29.
 */
public class WriteCompletionHandler extends AioCompletionHandler<Integer> {
    private final ByteBuffer buffer = ByteBuffer.allocate(4096);

    WriteCompletionHandler(AsynchronousSocketChannel channel) {
        super(channel);
    }

    @Override
    public void completed(Integer count, Action action) {
        try {
            JestfulAioClientRequest request = (JestfulAioClientRequest) action.getExtra().get(JestfulAioClientRequest.class);
            buffer.clear();
            if (request.send(buffer)) {
                AioListener listener = (AioListener) action.getExtra().get(AioListener.class);
                listener.onRequested(action);

                new ReadCompletionHandler(channel).completed(0, action);
            } else {
                buffer.flip();
                channel.write(buffer, action, this);
            }
        } catch (Exception e) {
            failed(e, action);
        }
    }
}
