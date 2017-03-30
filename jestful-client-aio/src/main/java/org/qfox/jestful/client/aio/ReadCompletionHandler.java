package org.qfox.jestful.client.aio;

import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.Action;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.concurrent.TimeUnit;

/**
 * Created by payne on 2017/3/29.
 */
public class ReadCompletionHandler extends AioCompletionHandler<Integer> {
    private final ByteBuffer buffer = ByteBuffer.allocate(4096);

    ReadCompletionHandler(AsynchronousSocketChannel channel, long timeout) {
        super(channel, timeout);
    }

    @Override
    public void completed(Integer count, Action action) {
        try {
            JestfulAioClientResponse response = (JestfulAioClientResponse) action.getExtra().get(JestfulAioClientResponse.class);
            buffer.flip();
            if (response.receive(buffer)) {
                AioListener listener = (AioListener) action.getExtra().get(AioListener.class);
                listener.onCompleted(action);

                IOKit.close(channel);
            } else {
                long timeAvailable = getTimeAvailable();
                if (timeAvailable <= 0) throw new InterruptedByTimeoutException();

                buffer.clear();
                channel.read(buffer, timeAvailable, TimeUnit.MILLISECONDS, action, this);
            }
        } catch (Exception e) {
            failed(e, action);
        }
    }
}
