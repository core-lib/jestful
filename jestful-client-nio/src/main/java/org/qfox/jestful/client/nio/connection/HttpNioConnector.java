package org.qfox.jestful.client.nio.connection;

import org.qfox.jestful.client.connection.HttpConnector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.nio.NioRequest;
import org.qfox.jestful.client.nio.NioResponse;
import org.qfox.jestful.core.Action;

import java.io.IOException;

/**
 * Created by payne on 2017/4/2.
 */
public class HttpNioConnector extends HttpConnector implements NioConnector {

    @Override
    public NioConnection nioConnect(Action action, Gateway gateway, NioClient client) throws IOException {
        NioRequest request = new JestfulNioHttpClientRequest(action, this, gateway, client.getConnTimeout(), client.getReadTimeout(), client.getWriteTimeout());
        NioResponse response = new JestfulNioHttpClientResponse(action, this, gateway);
        NioConnection connection = new NioConnection(request, response);

        // HTTP/1.1 要求不支持 Keep-Alive 的客户端必须在请求头声明 Connection: close 否则访问Github这样的网站就会有非常严重的性能问题
        Boolean keepAlive = client.getKeepAlive();
        if (keepAlive == null) request.setRequestHeader("Connection", "close");
        else if (keepAlive) request.setRequestHeader("Connection", "keep-alive");
        else request.setRequestHeader("Connection", "close");

        return connection;
    }

}
