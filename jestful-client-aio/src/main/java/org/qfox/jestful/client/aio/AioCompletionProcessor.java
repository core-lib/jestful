package org.qfox.jestful.client.aio;

import org.qfox.jestful.core.Action;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by yangchangpei on 17/3/29.
 */
public abstract class AioCompletionProcessor<V> implements CompletionHandler<V, Action> {
    protected final AsynchronousSocketChannel channel;

    protected AioCompletionProcessor(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void failed(Throwable exc, Action action) {

    }

}
