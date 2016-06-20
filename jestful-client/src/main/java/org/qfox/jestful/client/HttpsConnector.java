package org.qfox.jestful.client;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Restful;

/**
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年6月20日 下午4:13:47
 *
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

			HostnameVerifier hostnameVerifier = client.getHostnameVerifier();
			if (hostnameVerifier != null) {
				httpsURLConnection.setHostnameVerifier(hostnameVerifier);
			}
			
			SSLSocketFactory SSLSocketFactory = client.getSSLSocketFactory();
			if (SSLSocketFactory != null) {
				httpsURLConnection.setSSLSocketFactory(SSLSocketFactory);
			}

			Request request = new HttpRequest(httpsURLConnection);
			Response response = new HttpResponse(httpsURLConnection);
			return new Connection(request, response);
		} catch (Exception e) {
			error = true;
			throw new IOException(e);
		} finally {
			if (error != false && httpsURLConnection != null) {
				httpsURLConnection.disconnect();
			} else {
				gateway.onConnected(action);
			}
		}
	}

}
