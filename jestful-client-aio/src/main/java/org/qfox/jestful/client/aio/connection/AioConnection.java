package org.qfox.jestful.client.aio.connection;

import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.aio.AioOptions;
import org.qfox.jestful.client.aio.AioRequest;
import org.qfox.jestful.client.aio.AioResponse;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by payne on 2017/4/2.
 */
public abstract class AioConnection implements Closeable {
    protected final AioConnector connector;
    protected final AsynchronousSocketChannel channel;
    protected final SocketAddress address;
    protected AioRequest request;
    protected AioResponse response;
    protected volatile boolean connected;
    protected volatile boolean idled = true;
    protected volatile long timeExpired;

    public AioConnection(AioConnector connector, SocketAddress address, Action action, Gateway gateway, AioClient client) throws IOException {
        this.connector = connector;
        this.channel = AsynchronousSocketChannel.open(client.getAioChannelGroup());
        this.address = address;
        reset(action, gateway, client);
    }

    public void config(AioOptions options) throws IOException {
        options.config(channel);
    }

    public synchronized <A> void connect(A attachment, CompletionHandler<Void, ? super A> handler) {
        if (connected) throw new IllegalStateException("channel is connected");
        channel.connect(address, attachment, handler);
        connected = true;
    }

    public synchronized Future<Void> connect() {
        if (connected) throw new IllegalStateException("channel is connected");
        Future<Void> future = channel.connect(address);
        connected = true;
        return future;
    }

    public boolean isConnected() {
        return connected;
    }

    // ------------------- AsynchronousSocketChannel Delegate Methods Start ------------------ //

    public boolean isOpen() {
        return channel.isOpen();
    }

    public <A> void read(ByteBuffer dst, long timeout, TimeUnit unit, A attachment, CompletionHandler<Integer, ? super A> handler) {
        channel.read(dst, timeout, unit, attachment, handler);
    }

    public <A> void read(ByteBuffer dst, A attachment, CompletionHandler<Integer, ? super A> handler) {
        channel.read(dst, attachment, handler);
    }

    public Future<Integer> read(ByteBuffer dst) {
        return channel.read(dst);
    }

    public <A> void read(ByteBuffer[] dsts, int offset, int length, long timeout, TimeUnit unit, A attachment, CompletionHandler<Long, ? super A> handler) {
        channel.read(dsts, offset, length, timeout, unit, attachment, handler);
    }

    public <A> void write(ByteBuffer src, long timeout, TimeUnit unit, A attachment, CompletionHandler<Integer, ? super A> handler) {
        channel.write(src, timeout, unit, attachment, handler);
    }

    public <A> void write(ByteBuffer src, A attachment, CompletionHandler<Integer, ? super A> handler) {
        channel.write(src, attachment, handler);
    }

    public Future<Integer> write(ByteBuffer src) {
        return channel.write(src);
    }

    public <A> void write(ByteBuffer[] srcs, int offset, int length, long timeout, TimeUnit unit, A attachment, CompletionHandler<Long, ? super A> handler) {
        channel.write(srcs, offset, length, timeout, unit, attachment, handler);
    }

    // ------------------- AsynchronousSocketChannel Delegate Methods Start ------------------ //

    // ------------------- AioRequest Delegate Methods Start ------------------ //

    public void copy(ByteBuffer buffer) throws IOException {
        request.copy(buffer);
    }

    public boolean move(int n) throws IOException {
        return request.move(n);
    }

    public int getConnTimeout() {
        return request.getConnTimeout();
    }

    public void setConnTimeout(int timeout) {
        request.setConnTimeout(timeout);
    }

    public int getReadTimeout() {
        return request.getReadTimeout();
    }

    public void setReadTimeout(int timeout) {
        request.setReadTimeout(timeout);
    }

    public int getWriteTimeout() {
        return request.getWriteTimeout();
    }

    public void setWriteTimeout(int timeout) {
        request.setWriteTimeout(timeout);
    }

    // ------------------- AioRequest Delegate Methods End ------------------ //

    // ------------------- AioResponse Delegate Methods Start ------------------ //

    public boolean load(ByteBuffer buffer) throws IOException {
        return response.load(buffer);
    }

    // ------------------- AioResponse Delegate Methods End ------------------ //

    public boolean isKeepAlive() {
        return request.isKeepAlive() && response.isKeepAlive() && this.getIdleTimeout() != 0; // Keep-Alive: timeout=0 也认为是不保持长连接
    }

    public void setKeepAlive(boolean keepAlive) {
        request.setKeepAlive(keepAlive);
    }

    public int getIdleTimeout() {
        int reqIdleTimeout = request.getIdleTimeout();
        int respIdleTimeout = response.getIdleTimeout();
        return reqIdleTimeout < 0 || respIdleTimeout < 0 ? Math.max(reqIdleTimeout, respIdleTimeout) : Math.min(reqIdleTimeout, respIdleTimeout);
    }

    public void setIdleTimeout(int idleTimeout) {
        request.setIdleTimeout(idleTimeout);
    }

    /**
     * idle this connection. you should check whether this connection is can be keep alive
     * this method should call before release this connection every times
     *
     * @return time to expired
     */
    public final long idle() {
        if (idled) return this.timeExpired;
        idled = true;
        int idleTimeout = this.getIdleTimeout();
        if (idleTimeout >= 0) return this.timeExpired = System.currentTimeMillis() + idleTimeout * 1000L;// 有超时时间
        else return this.timeExpired = -1L;// 永不超时
    }

    public boolean available() {
        return !idled || this.timeExpired < 0 || this.timeExpired > System.currentTimeMillis(); // 当前正在使用或永不超时或还没超时
    }

    public void clear() {
        request.clear();
        response.clear();
    }

    /**
     * reset this connection.
     * this method should call before reuse this connection every times
     *
     * @param action  action
     * @param gateway gateway
     * @param client  client
     */
    public void reset(Action action, Gateway gateway, AioClient client) {
        if (!idled) return;
        idled = false;
    }

    public AioConnector getConnector() {
        return connector;
    }

    public AsynchronousSocketChannel getChannel() {
        return channel;
    }

    public SocketAddress getAddress() {
        return address;
    }

    public AioRequest getRequest() {
        return request;
    }

    public AioResponse getResponse() {
        return response;
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }

    @Override
    public String toString() {
        return channel.toString();
    }
}
