package org.qfox.jestful.client.aio;

import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.Action;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by yangchangpei on 17/3/29.
 */
public abstract class AioCompletionHandler<V> implements CompletionHandler<V, Action> {
    protected final AsynchronousSocketChannel channel;
    protected final long timeExpired;

    protected AioCompletionHandler(AsynchronousSocketChannel channel, long timeout) {
        if (channel == null) throw new IllegalArgumentException("asynchronous socket channel can not be null");
        if (timeout < 0) throw new IllegalArgumentException("timeout can not be negative");

        this.channel = channel;
        this.timeExpired = System.currentTimeMillis() + timeout;
    }

    @Override
    public void completed(V result, Action action) {
        try {
            onCompleted(result, action);
        } catch (Exception e) {
            onFailed(e, action);
        }
    }

    public abstract void onCompleted(V result, Action action) throws Exception;

    @Override
    public void failed(Throwable throwable, Action action) {
        try {
            throw throwable instanceof Exception ? (Exception) throwable : new Exception(throwable);
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
