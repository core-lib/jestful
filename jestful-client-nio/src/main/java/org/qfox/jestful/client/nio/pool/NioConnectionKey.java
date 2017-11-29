package org.qfox.jestful.client.nio.pool;

import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.nio.connection.NioConnector;
import org.qfox.jestful.core.Action;

import java.net.SocketAddress;

public class NioConnectionKey {
    final SocketAddress address;
    final NioConnector connector;
    final Action action;
    final Gateway gateway;
    final NioClient client;

    public NioConnectionKey(SocketAddress address) {
        this(address, null, null, null, null);
    }

    public NioConnectionKey(SocketAddress address, NioConnector connector, Action action, Gateway gateway, NioClient client) {
        if (address == null) throw new IllegalArgumentException("address must not be null");
        this.address = address;
        this.connector = connector;
        this.action = action;
        this.gateway = gateway;
        this.client = client;
    }

    public SocketAddress getAddress() {
        return address;
    }

    public NioConnector getConnector() {
        return connector;
    }

    public Action getAction() {
        return action;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public NioClient getClient() {
        return client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NioConnectionKey that = (NioConnectionKey) o;

        return address.equals(that.address);
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }

    @Override
    public String toString() {
        return address.toString();
    }
}
