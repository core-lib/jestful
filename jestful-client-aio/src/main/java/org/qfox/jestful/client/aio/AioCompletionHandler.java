package org.qfox.jestful.client.aio;

import org.qfox.jestful.client.aio.catcher.AioCatcher;
import org.qfox.jestful.client.aio.connection.AioConnection;
import org.qfox.jestful.client.aio.pool.AioConnectionPool;
import org.qfox.jestful.client.catcher.Catcher;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.exception.StatusException;

import java.net.SocketAddress;
import java.nio.channels.CompletionHandler;
import java.util.Map;

/**
 * Created by yangchangpei on 17/3/29.
 */
public abstract class AioCompletionHandler<V> implements CompletionHandler<V, Action> {
    protected final AioClient client;
    protected final AioConnection connection;
    protected final long timeExpired;

    protected AioCompletionHandler(AioClient client, AioConnection connection, long timeout) {
        if (client == null) throw new IllegalArgumentException("client can not be null");
        if (connection == null) throw new IllegalArgumentException("aio connection can not be null");
        if (timeout < 0) throw new IllegalArgumentException("timeout can not be negative");

        this.client = client;
        this.connection = connection;
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

    protected abstract void onCompleted(V result, Action action) throws Exception;

    @Override
    public void failed(Throwable throwable, Action action) {
        try {
            throw throwable instanceof Exception ? (Exception) throwable : new Exception(throwable);
        } catch (StatusException statusException) {
            try {
                Map<String, Catcher> catchers = client.getCatchers();
                for (Catcher catcher : catchers.values()) {
                    if (catcher instanceof AioCatcher && catcher.catchable(statusException)) {
                        ((AioCatcher) catcher).aioCaught(client, action, statusException);
                        return;
                    }
                }
                throw statusException;
            } catch (Exception e) {
                onFailed(e, action);
            }
        } catch (Exception e) {
            onFailed(e, action);
        }
    }

    protected void onFailed(Exception exception, Action action) {
        AioEventListener listener = (AioEventListener) action.getExtra().get(AioEventListener.class);
        listener.onException(action, exception);
        this.release();
    }

    protected void release() {
        // 回收Keep Alive连接
        if (connection.isOpen() && connection.isConnected() && connection.isKeepAlive()) {
            SocketAddress address = connection.getAddress();
            AioConnectionPool connectionPool = client.getConnectionPool();
            connectionPool.release(address, connection);
        } else {
            IOKit.close(connection);
        }
    }

    protected long toTimeAvailable() {
        return timeExpired - System.currentTimeMillis();
    }

}
