package org.qfox.jestful.client.aio;

import org.qfox.jestful.core.Action;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by yangchangpei on 17/3/29.
 */
public class ConnectCompletionProcessor extends AioCompletionProcessor<Void> {

    public ConnectCompletionProcessor(AsynchronousSocketChannel channel) {
        super(channel);
    }

    @Override
    public void completed(Void result, Action attachment) {

    }

}
