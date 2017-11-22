package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.Action;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by payne on 2017/4/1.
 * Version: 1.0
 */
public class JestfulNioHttpsClientRequest extends JestfulNioHttpClientRequest {
    private final NioSSLChannel nioSSLChannel;

    protected JestfulNioHttpsClientRequest(Action action,
                                           Connector connector,
                                           Gateway gateway,
                                           int connTimeout,
                                           int readTimeout,
                                           int writeTimeout,
                                           NioSSLChannel nioSSLChannel) {
        super(action, connector, gateway, connTimeout, readTimeout, writeTimeout);
        this.nioSSLChannel = nioSSLChannel;
    }

    // 数据出站
    @Override
    public void copy(ByteBuffer buffer) throws IOException {
        if (head == null) {
            doWriteHeader();
        }

        nioSSLChannel.write(head);
        nioSSLChannel.write(body);

        nioSSLChannel.copy(buffer);
    }


    @Override
    public boolean move(int n) throws IOException {
        return nioSSLChannel.move(n) && head.remaining() == 0 && body.remaining() == 0;
    }

    @Override
    public void close() throws IOException {
        super.close();
        IOKit.close(nioSSLChannel);
    }
}
