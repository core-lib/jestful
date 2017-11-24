package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.HttpsConnector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.nio.NioRequest;
import org.qfox.jestful.client.nio.NioResponse;
import org.qfox.jestful.core.Action;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.IOException;

/**
 * Created by payne on 2017/4/2.
 * Version: 1.0
 */
public class HttpsNioConnector extends HttpsConnector implements NioConnector {
    private final Object lock = new Object();
    private SSLContext context;

    @Override
    public NioConnection nioConnect(Action action, Gateway gateway, NioClient client) throws IOException {
        SSLContext sslContext = client.getSslContext();
        sslContext = sslContext != null ? sslContext : getDefaultSSLContext();
        SSLEngine engine = sslContext.createSSLEngine();
        engine.setUseClientMode(true);
        engine.beginHandshake();
        NioSSLChannel nioSSLChannel = new JestfulNioSSLChannel(engine);
        NioRequest request = new JestfulNioHttpsClientRequest(action, this, gateway, client.getConnTimeout(), client.getReadTimeout(), client.getWriteTimeout(), nioSSLChannel);
        NioResponse response = new JestfulNioHttpsClientResponse(action, this, gateway, nioSSLChannel);
        NioConnection connection = new NioConnection(request, response);

        // HTTP/1.1 要求不支持 Keep-Alive 的客户端必须在请求头声明 Connection: close 否则访问Github这样的网站就会有非常严重的性能问题
        Boolean keepAlive = client.getKeepAlive();
        if (keepAlive == null) request.setRequestHeader("Connection", "close");
        else if (keepAlive) request.setRequestHeader("Connection", "keep-alive");
        else request.setRequestHeader("Connection", "close");

        return connection;
    }

    private SSLContext getDefaultSSLContext() throws IOException {
        if (context != null) return context;
        synchronized (lock) {
            if (context != null) return context;
            try {
                context = SSLContext.getInstance("SSL");
                context.init(null, null, null);
                return context;
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
    }

}
