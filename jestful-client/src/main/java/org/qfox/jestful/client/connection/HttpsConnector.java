package org.qfox.jestful.client.connection;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Restful;
import org.qfox.jestful.core.exception.JestfulIOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年6月20日 下午4:13:47
 * @since 1.0.0
 */
public class HttpsConnector implements Connector {

    public boolean supports(Action action) {
        String protocol = action.getProtocol();
        return "https".equalsIgnoreCase(protocol);
    }

    public Connection connect(Action action, Gateway gateway, Client client) throws IOException {
        boolean error = false;
        HttpsURLConnection httpsURLConnection = null;
        try {
            String url = action.getURL();
            httpsURLConnection = (HttpsURLConnection) new URL(url).openConnection(gateway.isProxy() ? gateway.toProxy() : Proxy.NO_PROXY);
            Restful restful = action.getRestful();
            httpsURLConnection.setRequestMethod(restful.getMethod());
            httpsURLConnection.setDoOutput(restful.isAcceptBody());
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setInstanceFollowRedirects(false);

            HostnameVerifier hostnameVerifier = client.getHostnameVerifier();
            if (hostnameVerifier != null) httpsURLConnection.setHostnameVerifier(hostnameVerifier);

            SSLSocketFactory SSLSocketFactory = client.getSSLSocketFactory();
            if (SSLSocketFactory != null) httpsURLConnection.setSSLSocketFactory(SSLSocketFactory);

            Request request = new JestfulHttpsClientRequest(httpsURLConnection);
            Response response = new JestfulHttpsClientResponse(httpsURLConnection);
            Connection connection = new Connection(request, response);

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
            if (error && httpsURLConnection != null) httpsURLConnection.disconnect();
            else gateway.onConnected(action);
        }
    }

}
