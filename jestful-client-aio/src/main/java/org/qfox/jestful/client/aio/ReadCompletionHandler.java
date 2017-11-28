package org.qfox.jestful.client.aio;

import org.qfox.jestful.client.aio.connection.AioConnection;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.Action;

import java.nio.ByteBuffer;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.concurrent.TimeUnit;

/**
 * Created by payne on 2017/3/29.
 */
public class ReadCompletionHandler extends AioCompletionHandler<Integer> {
    private final ByteBuffer buffer = ByteBuffer.allocate(4096);

    ReadCompletionHandler(AioClient client, AioConnection connection, long timeout) {
        super(client, connection, timeout);
    }

    @Override
    public void onCompleted(Integer count, Action action) throws Exception {
        buffer.flip();
        if (connection.load(buffer)) {
            AioEventListener listener = (AioEventListener) action.getExtra().get(AioEventListener.class);
            listener.onCompleted(action);

            IOKit.close(connection);
        } else {
            long timeAvailable = toTimeAvailable();
            if (timeAvailable <= 0) throw new InterruptedByTimeoutException();

            buffer.clear();
            connection.read(buffer, timeAvailable, TimeUnit.MILLISECONDS, action, this);
        }
    }
}
