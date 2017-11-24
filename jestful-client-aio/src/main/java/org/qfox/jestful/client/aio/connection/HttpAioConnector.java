package org.qfox.jestful.client.aio.connection;

import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.aio.AioRequest;
import org.qfox.jestful.client.aio.AioResponse;
import org.qfox.jestful.client.connection.HttpConnector;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

import java.io.IOException;

/**
 * Created by payne on 2017/4/2.
 */
public class HttpAioConnector extends HttpConnector implements AioConnector {

    @Override
    public AioConnection aioConnect(Action action, Gateway gateway, AioClient client) throws IOException {
        AioRequest request = new JestfulAioHttpClientRequest(action, this, gateway, client.getConnTimeout(), client.getReadTimeout(), client.getWriteTimeout());
        AioResponse response = new JestfulAioHttpClientResponse(action, this, gateway);
        AioConnection connection = new AioConnection(request, response);

        // HTTP/1.1 要求不支持 Keep-Alive 的客户端必须在请求头声明 Connection: close 否则访问Github这样的网站就会有非常严重的性能问题
        Boolean keepAlive = client.getKeepAlive();
        if (keepAlive == null) request.setRequestHeader("Connection", "close");
        else if (keepAlive) request.setRequestHeader("Connection", "keep-alive");
        else request.setRequestHeader("Connection", "close");

        return connection;
    }

}
