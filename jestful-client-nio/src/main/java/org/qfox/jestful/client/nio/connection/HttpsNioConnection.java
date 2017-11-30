package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.core.Action;

import java.io.IOException;
import java.net.SocketAddress;

public class HttpsNioConnection extends NioConnection {
    private final NioSSLChannel nioSSLChannel;

    public HttpsNioConnection(NioConnector connector, SocketAddress address, Action action, Gateway gateway, NioClient client, NioSSLChannel nioSSLChannel) throws IOException {
        super(connector, address, action, gateway, client);
        this.nioSSLChannel = nioSSLChannel;
        reset(action, gateway, client);
    }

    @Override
    public boolean reset(Action action, Gateway gateway, NioClient client) {
        if (nioSSLChannel == null) return false;
        if (super.reset(action, gateway, client)) {
            nioSSLChannel.reset();
            request = new JestfulNioHttpsClientRequest(action, connector, gateway, client.getConnTimeout(), client.getReadTimeout(), client.getWriteTimeout(), nioSSLChannel);
            response = new JestfulNioHttpsClientResponse(action, connector, gateway, nioSSLChannel);
            return true;
        }
        return false;
    }

    @Override
    public void close() throws IOException {
        super.close();
        nioSSLChannel.close();
    }
}
