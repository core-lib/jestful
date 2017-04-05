package org.qfox.jestful.client.nio.timeout;

import org.qfox.jestful.client.nio.NioEventListener;
import org.qfox.jestful.core.Action;

import java.nio.channels.SelectionKey;

/**
 * Created by yangchangpei on 17/3/27.
 */
public class ConnTimeoutHandler extends TimeoutHandler {

    protected ConnTimeoutHandler(SelectionKey key, long timeout) {
        super(key, timeout);
    }

    @Override
    public boolean isInvalid() {
        return (key.interestOps() & SelectionKey.OP_CONNECT) != 0;
    }

    @Override
    public void doTimeout() {
        Action action = (Action) key.attachment();
        key.cancel();
        NioEventListener listener = (NioEventListener) action.getExtra().get(NioEventListener.class);
        listener.onException(action, toTimeoutException("connect timeout"));
    }
}
