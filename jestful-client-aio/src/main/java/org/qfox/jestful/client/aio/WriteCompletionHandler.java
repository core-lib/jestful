package org.qfox.jestful.client.aio;

import org.qfox.jestful.core.Action;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.concurrent.TimeUnit;

/**
 * Created by payne on 2017/3/29.
 */
public class WriteCompletionHandler extends AioCompletionHandler<Integer> {
    private final ByteBuffer buffer = ByteBuffer.allocate(4096);

    WriteCompletionHandler(AsynchronousSocketChannel channel, long timeout) {
        super(channel, timeout);
    }

    @Override
    public void completed(Integer count, Action action) {
        try {
            JestfulAioClientRequest request = (JestfulAioClientRequest) action.getExtra().get(JestfulAioClientRequest.class);
            buffer.clear();
            if (request.send(buffer)) {
                AioListener listener = (AioListener) action.getExtra().get(AioListener.class);
                listener.onRequested(action);

                new ReadCompletionHandler(channel, request.getReadTimeout()).completed(0, action);
            } else {
                long timeAvailable = getTimeAvailable();
                if (timeAvailable <= 0) throw new InterruptedByTimeoutException();

                buffer.flip();
                channel.write(buffer, timeAvailable, TimeUnit.MILLISECONDS, action, this);
            }
        } catch (Exception e) {
            failed(e, action);
        }
    }
}
