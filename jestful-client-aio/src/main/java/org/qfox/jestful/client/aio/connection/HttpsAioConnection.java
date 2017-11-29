package org.qfox.jestful.client.aio.connection;

import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

import java.io.IOException;
import java.net.SocketAddress;

public class HttpsAioConnection extends AioConnection {
    private final AioSSLChannel nioSSLChannel;

    public HttpsAioConnection(AioConnector connector, SocketAddress address, Action action, Gateway gateway, AioClient client, AioSSLChannel nioSSLChannel) throws IOException {
        super(connector, address, action, gateway, client);
        this.nioSSLChannel = nioSSLChannel;
        reset(action, gateway, client);
    }

    @Override
    public void reset(Action action, Gateway gateway, AioClient client) {
        super.reset(action, gateway, client);
        if (nioSSLChannel == null) return;
        nioSSLChannel.reset();
        request = new JestfulAioHttpsClientRequest(action, connector, gateway, client.getConnTimeout(), client.getReadTimeout(), client.getWriteTimeout(), nioSSLChannel);
        response = new JestfulAioHttpsClientResponse(action, connector, gateway, nioSSLChannel);
    }

    @Override
    public void close() throws IOException {
        super.close();
        nioSSLChannel.close();
    }
}
