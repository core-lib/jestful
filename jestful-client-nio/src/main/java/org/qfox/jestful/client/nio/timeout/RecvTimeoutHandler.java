package org.qfox.jestful.client.nio.timeout;

import org.qfox.jestful.client.nio.NioEventListener;
import org.qfox.jestful.core.Action;

import java.nio.channels.SelectionKey;

/**
 * Created by yangchangpei on 17/3/27.
 */
public class RecvTimeoutHandler extends TimeoutHandler {

    protected RecvTimeoutHandler(SelectionKey key, long timeout) {
        super(key, timeout);
    }

    @Override
    public boolean isChanged() {
        return super.isChanged() || (key.interestOps() & SelectionKey.OP_READ) == 0;
    }

    @Override
    public void doTimeout() {
        super.doTimeout();
        Action action = (Action) key.attachment();
        NioEventListener listener = (NioEventListener) action.getExtra().get(NioEventListener.class);
        listener.onException(action, toTimeoutException("read timeout"));
    }
}
