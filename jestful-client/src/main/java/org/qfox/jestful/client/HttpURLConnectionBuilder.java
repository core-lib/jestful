package org.qfox.jestful.client;

import java.net.HttpURLConnection;
import java.net.URL;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
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
 * @date 2016年4月28日 下午7:31:23
 *
 * @since 1.0.0
 */
public class HttpURLConnectionBuilder implements Actor {

	public Object react(Action action) throws Exception {
		String protocol = action.getProtocol();
		if ("http".equalsIgnoreCase(protocol)) {
			String host = action.getHost();
			Integer port = action.getPort();
			String route = action.getRoute();
			String URI = action.getURI();
			String query = action.getQuery();
			String url = protocol + "://" + host + (port != null ? ":" + port : "") + (route != null ? route : "") + URI + (query != null ? "?" + query : "");
			HttpURLConnection httpURLConnection = null;
			try {
				httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
				Restful restful = action.getRestful();
				httpURLConnection.setDoOutput(restful.isAcceptBody());
				httpURLConnection.setDoInput(restful.isReturnBody());
				Request request = new HttpRequest(httpURLConnection);
				Response response = new HttpResponse(httpURLConnection);
				action.setRequest(request);
				action.setResponse(response);
			} catch (Exception e) {
				throw e;
			} finally {
				httpURLConnection.disconnect();
			}
		}
		return action.execute();
	}
}
