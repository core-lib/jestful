package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.Connection;
import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
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
public class NioConnection extends Connection implements Closeable {
    private final SocketChannel channel;

    public NioConnection(SocketAddress address, NioRequest request, NioResponse response) throws IOException {
        super(address, request, response);
        this.channel = SocketChannel.open();
        this.channel.configureBlocking(false);
    }

    public void config(NioOptions options) throws IOException {
        options.config(channel);
    }

    public void connect() throws IOException {
        channel.connect(address);
    }

    // ------------------- SocketChannel Delegate Methods Start ------------------ //

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
        getRequest().copy(buffer);
    }

    public boolean move(int n) throws IOException {
        return getRequest().move(n);
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
        return getResponse().load(buffer);
    }

    // ------------------- NioResponse Delegate Methods End ------------------ //

    public boolean isKeepAlive() {
        return getRequest().isKeepAlive() && getResponse().isKeepAlive();
    }

    public void setKeepAlive(boolean keepAlive) {
        getRequest().setKeepAlive(keepAlive);
    }

    public void clear() {
        getRequest().clear();
        getResponse().clear();
    }

    public void reset(Action action, Connector connector, Gateway gateway, int connTimeout, int readTimeout, int writeTimeout) {
        getRequest().reset(action, connector, gateway, connTimeout, readTimeout, writeTimeout);
        getResponse().reset(action, connector, gateway);
    }

    @Override
    public NioRequest getRequest() {
        return (NioRequest) request;
    }

    @Override
    public NioResponse getResponse() {
        return (NioResponse) response;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    @Override
    public void close() throws IOException {

    }
}
