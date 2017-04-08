package org.qfox.jestful.client.aio;

import org.qfox.jestful.client.aio.catcher.AioCatcher;
import org.qfox.jestful.client.catcher.Catcher;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.exception.StatusException;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Map;

/**
 * Created by yangchangpei on 17/3/29.
 */
public abstract class AioCompletionHandler<V> implements CompletionHandler<V, Action> {
    protected final AioClient client;
    protected final AsynchronousSocketChannel channel;
    protected final long timeExpired;

    protected AioCompletionHandler(AioClient client, AsynchronousSocketChannel channel, long timeout) {
        if (client == null) throw new IllegalArgumentException("client can not be null");
        if (channel == null) throw new IllegalArgumentException("asynchronous socket channel can not be null");
        if (timeout < 0) throw new IllegalArgumentException("timeout can not be negative");

        this.client = client;
        this.channel = channel;
        this.timeExpired = System.currentTimeMillis() + timeout;
    }

    @Override
    public void completed(V result, Action action) {
        try {
            onCompleted(result, action);
        } catch (Exception e) {
            failed(e, action);
        }
    }

    public abstract void onCompleted(V result, Action action) throws Exception;

    @Override
    public void failed(Throwable throwable, Action action) {
        try {
            throw throwable instanceof Exception ? (Exception) throwable : new Exception(throwable);
        } catch (StatusException statusException) {
            try {
                Map<String, Catcher> catchers = client.getCatchers();
                for (Catcher catcher : catchers.values()) {
                    if (catcher instanceof AioCatcher && catcher.catchable(statusException)) {
                        ((AioCatcher) catcher).aioCatched(client, action, statusException);
                        break;
                    }
                }
            } catch (Exception e) {
                onFailed(e, action);
            }
        } catch (Exception e) {
            onFailed(e, action);
        }
    }

    public void onFailed(Exception exception, Action action) {
        AioEventListener listener = (AioEventListener) action.getExtra().get(AioEventListener.class);
        listener.onException(action, exception);
        if (channel.isOpen()) IOKit.close(channel);
    }

    protected long toTimeAvailable() {
        return timeExpired - System.currentTimeMillis();
    }

}
