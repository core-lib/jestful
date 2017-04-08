package org.qfox.jestful.client.connection;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Restful;
import org.qfox.jestful.core.exception.JestfulIOException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

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
            Request request = new HttpRequest(httpURLConnection);
            Response response = new HttpResponse(httpURLConnection);
            return new Connection(request, response);
        } catch (Exception e) {
            error = true;
            throw new JestfulIOException(e);
        } finally {
            if (error != false && httpURLConnection != null) {
                httpURLConnection.disconnect();
            } else {
                gateway.onConnected(action);
            }
        }
    }

}
