package org.qfox.jestful.client.nio.pool;

import org.qfox.jestful.client.nio.connection.NioConnection;

import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class SocketChannelConnection {
    private final SocketAddress address;
    private final SocketChannel channel;
    private final NioConnection connection;

    public SocketChannelConnection(SocketAddress address, SocketChannel channel, NioConnection connection) {
        this.address = address;
        this.channel = channel;
        this.connection = connection;
    }

    public SocketAddress getAddress() {
        return address;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public NioConnection getConnection() {
        return connection;
    }

}
