package org.qfox.jestful.client.connection;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Restful;
import org.qfox.jestful.core.exception.JestfulIOException;

import java.io.IOException;
import java.net.*;

public class HttpConnector implements Connector {

    public boolean supports(Action action) {
        String protocol = action.getProtocol();
        return "http".equalsIgnoreCase(protocol);
    }

    public Connection connect(Action action, Gateway gateway, Client client) throws IOException {
        boolean error = false;
        HttpURLConnection httpURLConnection = null;
        try {
            String url = action.getURL();

            httpURLConnection = (HttpURLConnection) new URL(url).openConnection(gateway.isProxy() ? gateway.toProxy() : Proxy.NO_PROXY);
            Restful restful = action.getRestful();
            httpURLConnection.setRequestMethod(restful.getMethod());
            httpURLConnection.setDoOutput(restful.isAcceptBody());
            httpURLConnection.setDoInput(true);
            httpURLConnection.setInstanceFollowRedirects(false);
            SocketAddress address = address(action, gateway, client);
            Request request = new JestfulHttpClientRequest(httpURLConnection);
            Response response = new JestfulHttpClientResponse(httpURLConnection);
            Connection connection = new Connection(address, request, response);

            // HTTP/1.1 要求不支持 Keep-Alive 的客户端必须在请求头声明 Connection: close 否则访问Github这样的网站就会有非常严重的性能问题
            Boolean keepAlive = client.getKeepAlive();
            if (keepAlive == null) return connection;
            else if (keepAlive) request.setRequestHeader("Connection", "keep-alive");
            else request.setRequestHeader("Connection", "close");

            return connection;
        } catch (Exception e) {
            error = true;
            throw new JestfulIOException(e);
        } finally {
            if (error && httpURLConnection != null) httpURLConnection.disconnect();
            else gateway.onConnected(action);
        }
    }

    @Override
    public SocketAddress address(Action action, Gateway gateway, Client client) {
        String host = action.getHostname();
        Integer port = action.getPort();
        port = port != null && port >= 0 ? port : 80;
        return gateway != null && gateway.isProxy() ? gateway.toSocketAddress() : new InetSocketAddress(host, port);
    }
}
