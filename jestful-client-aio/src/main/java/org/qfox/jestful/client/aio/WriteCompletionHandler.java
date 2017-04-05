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
    public void onCompleted(Integer count, Action action) throws Exception {
        JestfulAioClientRequest request = (JestfulAioClientRequest) action.getExtra().get(JestfulAioClientRequest.class);
        buffer.clear();
        if (request.move(count)) {
            AioEventListener listener = (AioEventListener) action.getExtra().get(AioEventListener.class);
            listener.onRequested(action);

            new ReadCompletionHandler(channel, request.getReadTimeout()).completed(0, action);
        } else {
            long timeAvailable = toTimeAvailable();
            if (timeAvailable <= 0) throw new InterruptedByTimeoutException();

            request.copy(buffer);
            buffer.flip();
            channel.write(buffer, timeAvailable, TimeUnit.MILLISECONDS, action, this);
        }
    }
}
