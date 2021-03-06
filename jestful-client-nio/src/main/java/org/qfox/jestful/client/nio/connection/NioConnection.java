package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.nio.NioOptions;
import org.qfox.jestful.client.nio.NioRequest;
import org.qfox.jestful.client.nio.NioResponse;
import org.qfox.jestful.core.Action;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Created by payne on 2017/4/2.
 */
public abstract class NioConnection implements Closeable {
    protected final NioConnector connector;
    protected final SocketChannel channel;
    protected final SocketAddress address;
    protected volatile NioRequest request;
    protected volatile NioResponse response;
    protected volatile boolean idled = true;
    protected volatile long timeExpired;

    public NioConnection(NioConnector connector, SocketAddress address, Action action, Gateway gateway, NioClient client) throws IOException {
        this.connector = connector;
        this.channel = SocketChannel.open();
        this.channel.configureBlocking(false);
        this.address = address;
        reset(action, gateway, client);
    }

    public void config(NioOptions options) throws IOException {
        options.config(channel);
    }

    public void connect() throws IOException {
        channel.connect(address);
    }

    // ------------------- SocketChannel Delegate Methods Start ------------------ //

    public boolean isOpen() {
        return channel.isOpen();
    }

    public boolean isConnected() {
        return channel.isConnected();
    }

    public boolean isConnectionPending() {
        return channel.isConnectionPending();
    }

    public boolean finishConnect() throws IOException {
        return channel.finishConnect();
    }

    public SelectionKey register(Selector sel, int ops, Object att) throws ClosedChannelException {
        return channel.register(sel, ops, att);
    }

    public SelectionKey register(Selector sel, int ops) throws ClosedChannelException {
        return channel.register(sel, ops);
    }

    public SelectionKey keyFor(Selector sel) {
        return channel.keyFor(sel);
    }

    public int read(ByteBuffer dst) throws IOException {
        return channel.read(dst);
    }

    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        return channel.read(dsts, offset, length);
    }

    public long read(ByteBuffer[] dsts) throws IOException {
        return channel.read(dsts);
    }

    public int write(ByteBuffer src) throws IOException {
        return channel.write(src);
    }

    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        return channel.write(srcs, offset, length);
    }

    public long write(ByteBuffer[] srcs) throws IOException {
        return channel.write(srcs);
    }

    // ------------------- SocketChannel Delegate Methods End ------------------ //

    // ------------------- NioRequest Delegate Methods Start ------------------ //

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

    // ------------------- NioRequest Delegate Methods End ------------------ //

    // ------------------- NioResponse Delegate Methods Start ------------------ //

    public boolean load(ByteBuffer buffer) throws IOException {
        return response.load(buffer);
    }

    // ------------------- NioResponse Delegate Methods End ------------------ //

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
     * @return if the connection had already bean reset return {@code false} else {@code true}
     */
    public boolean reset(Action action, Gateway gateway, NioClient client) {
        if (!idled) return false;
        idled = false;
        return true;
    }

    public NioConnector getConnector() {
        return connector;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public SocketAddress getAddress() {
        return address;
    }

    public NioRequest getRequest() {
        return request;
    }

    public NioResponse getResponse() {
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
