package org.qfox.jestful.client.aio;

import org.qfox.jestful.commons.IOUtils;
import org.qfox.jestful.core.Action;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by payne on 2017/3/29.
 */
public class ReadCompletionHandler extends AioCompletionHandler<Integer> {
    private final ByteBuffer buffer = ByteBuffer.allocate(4096);

    ReadCompletionHandler(AsynchronousSocketChannel channel) {
        super(channel);
        this.buffer.flip();
    }

    @Override
    public void completed(Integer count, Action action) {
        try {
            JestfulAioClientResponse response = (JestfulAioClientResponse) action.getExtra().get(JestfulAioClientResponse.class);
            if (response.receive(buffer)) {
                AioListener listener = (AioListener) action.getExtra().get(AioListener.class);
                listener.onCompleted(action);

                IOUtils.close(channel);
            } else {
                channel.read(buffer, action, this);
            }
        } catch (Exception e) {
            failed(e, action);
        }
    }
}
