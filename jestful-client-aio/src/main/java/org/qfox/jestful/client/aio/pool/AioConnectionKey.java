package org.qfox.jestful.client.aio.pool;

import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.aio.connection.AioConnector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

import java.net.SocketAddress;

public class AioConnectionKey {
    final SocketAddress address;
    final AioConnector connector;
    final Action action;
    final Gateway gateway;
    final AioClient client;

    public AioConnectionKey(SocketAddress address) {
        this(address, null, null, null, null);
    }

    public AioConnectionKey(SocketAddress address, AioConnector connector, Action action, Gateway gateway, AioClient client) {
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

    public AioConnector getConnector() {
        return connector;
    }

    public Action getAction() {
        return action;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public AioClient getClient() {
        return client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AioConnectionKey that = (AioConnectionKey) o;

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
