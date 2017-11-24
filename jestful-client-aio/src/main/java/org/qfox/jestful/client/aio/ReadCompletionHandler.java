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

    ReadCompletionHandler(AioClient client, AsynchronousSocketChannel channel, long timeout) {
        super(client, channel, timeout);
    }

    @Override
    public void onCompleted(Integer count, Action action) throws Exception {
        AioResponse response = (AioResponse) action.getExtra().get(AioResponse.class);
        buffer.flip();
        if (response.load(buffer)) {
            AioEventListener listener = (AioEventListener) action.getExtra().get(AioEventListener.class);
            listener.onCompleted(action);

            IOKit.close(channel);
        } else {
            long timeAvailable = toTimeAvailable();
            if (timeAvailable <= 0) throw new InterruptedByTimeoutException();

            buffer.clear();
            channel.read(buffer, timeAvailable, TimeUnit.MILLISECONDS, action, this);
        }
    }
}
