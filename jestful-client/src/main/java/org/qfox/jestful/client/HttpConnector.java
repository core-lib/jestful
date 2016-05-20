package org.qfox.jestful.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Restful;

public class HttpConnector implements Connector {

	public boolean supports(Action action) {
		String protocol = action.getProtocol();
		return "http".equalsIgnoreCase(protocol);
	}

	public Connection connect(Action action) throws IOException {
		boolean error = false;
		HttpURLConnection httpURLConnection = null;
		try {
			String url = action.getURL();
			httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
			Restful restful = action.getRestful();
			httpURLConnection.setDoOutput(restful.isAcceptBody());
			httpURLConnection.setDoInput(restful.isReturnBody());
			Request request = new HttpRequest(httpURLConnection);
			Response response = new HttpResponse(httpURLConnection);
			return new Connection(request, response);
		} catch (Exception e) {
			error = true;
			throw new IOException(e);
		} finally {
			if (error != false && httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
	}

}
