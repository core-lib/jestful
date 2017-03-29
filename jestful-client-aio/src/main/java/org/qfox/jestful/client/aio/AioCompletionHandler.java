package org.qfox.jestful.client.aio;

import org.qfox.jestful.core.Action;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by yangchangpei on 17/3/29.
 */
public abstract class AioCompletionHandler<V> implements CompletionHandler<V, Action> {
    protected final AsynchronousSocketChannel channel;

    protected AioCompletionHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void failed(Throwable throwable, Action action) {
        AioListener listener = (AioListener) action.getExtra().get(AioListener.class);
        listener.onException(action, throwable instanceof Exception ? (Exception) throwable : new Exception(throwable));
    }

}
