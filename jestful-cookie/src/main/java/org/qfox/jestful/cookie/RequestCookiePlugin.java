package org.qfox.jestful.cookie;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Request;

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
 * @date 2016年5月10日 下午7:58:10
 *
 * @since 1.0.0
 */
public class RequestCookiePlugin implements Actor {
	private CookieHandler cookieHandler;

	public RequestCookiePlugin() {
		super();
		this.cookieHandler = CookieHandler.getDefault();
		this.cookieHandler = cookieHandler != null ? cookieHandler : new CookieManager();
	}

	public RequestCookiePlugin(CookieHandler cookieHandler) {
		super();
		this.cookieHandler = cookieHandler;
	}

	public Object react(Action action) throws Exception {
		Request request = action.getRequest();
		URL url = new URL(action.getURL());
		URI uri = url.toURI();
		Map<String, List<String>> header = new HashMap<String, List<String>>();
		String[] keys = request.getHeaderKeys();
		for (String key : keys) {
			String[] values = request.getRequestHeaders(key);
			header.put(key, Arrays.asList(values));
		}
		Map<String, List<String>> map = cookieHandler.get(uri, header);
		for (Entry<String, List<String>> entry : map.entrySet()) {
			request.setRequestHeaders(entry.getKey(), entry.getValue().toArray(new String[0]));
		}
		return action.execute();
	}

	public CookieHandler getCookieHandler() {
		return cookieHandler;
	}

	public void setCookieHandler(CookieHandler cookieHandler) {
		this.cookieHandler = cookieHandler;
	}

}
