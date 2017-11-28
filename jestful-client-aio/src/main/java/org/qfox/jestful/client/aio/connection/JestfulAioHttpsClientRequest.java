package org.qfox.jestful.client.aio.connection;

import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by payne on 2017/4/1.
 * Version: 1.0
 */
public class JestfulAioHttpsClientRequest extends JestfulAioHttpClientRequest {
    private final AioSSLChannel aioSSLChannel;

    protected JestfulAioHttpsClientRequest(Action action,
                                           Connector connector,
                                           Gateway gateway,
                                           int connTimeout,
                                           int readTimeout,
                                           int writeTimeout,
                                           AioSSLChannel aioSSLChannel) {
        super(action, connector, gateway, connTimeout, readTimeout, writeTimeout);
        this.aioSSLChannel = aioSSLChannel;
    }

    // 数据出站
    @Override
    public void copy(ByteBuffer buffer) throws IOException {
        if (head == null) {
            doWriteHeader();
        }

        aioSSLChannel.write(head);
        aioSSLChannel.write(body);

        aioSSLChannel.copy(buffer);
    }


    @Override
    public boolean move(int n) throws IOException {
        return aioSSLChannel.move(n) && head.remaining() == 0 && body.remaining() == 0;
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
