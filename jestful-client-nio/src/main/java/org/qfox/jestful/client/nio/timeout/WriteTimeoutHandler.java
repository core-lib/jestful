package org.qfox.jestful.client.nio.timeout;

import org.qfox.jestful.client.nio.NioListener;
import org.qfox.jestful.core.Action;

import java.nio.channels.SelectionKey;

/**
 * Created by yangchangpei on 17/3/27.
 */
public class WriteTimeoutHandler extends TimeoutHandler {

    protected WriteTimeoutHandler(SelectionKey key, long timeout) {
        super(key, timeout);
    }

    @Override
    public void doInvalid() {
        Action action = (Action) key.attachment();
        key.cancel();
        NioListener listener = (NioListener) action.getExtra().get(NioListener.class);
        listener.onException(action, wrapSocketTimeoutException("write timeout"));
    }
}
