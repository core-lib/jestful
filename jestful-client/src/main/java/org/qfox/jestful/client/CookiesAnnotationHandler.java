package org.qfox.jestful.client;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.qfox.jestful.client.annotation.Cookies;
import org.qfox.jestful.client.exception.IllegalCookieException;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Resource;
import org.qfox.jestful.core.converter.StringConverter;

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
 * @date 2016年4月28日 下午9:41:34
 *
 * @since 1.0.0
 */
public class CookiesAnnotationHandler implements Actor {
	private final List<StringConverter<?>> converters = new ArrayList<StringConverter<?>>();

	public Object react(Action action) throws Exception {
		Request request = action.getRequest();
		String cookie = request.getRequestHeader("Cookie");
		cookie = cookie != null ? cookie : "";
		String charset = action.getCharset();
		Resource resource = action.getResource();
		if (resource.isAnnotationPresent(Cookies.class)) {
			Cookies queries = resource.getAnnotation(Cookies.class);
			String[] values = queries.value();
			for (String value : values) {
				String[] keyvalue = value.split("=");
				if (keyvalue.length != 2) {
					throw new IllegalCookieException(value + " is not a key-value pair like key=value", value);
				}
				cookie += (cookie.isEmpty() ? "" : "; ") + URLEncoder.encode(keyvalue[0], charset) + "=" + URLEncoder.encode(keyvalue[1], charset);
			}
		}

		Mapping mapping = action.getMapping();
		if (mapping.isAnnotationPresent(Cookies.class)) {
			Cookies queries = mapping.getAnnotation(Cookies.class);
			String[] values = queries.value();
			for (String value : values) {
				String[] keyvalue = value.split("=");
				if (keyvalue.length != 2) {
					throw new IllegalCookieException(value + " is not a key-value pair like key=value", value);
				}
				cookie += (cookie.isEmpty() ? "" : "; ") + URLEncoder.encode(keyvalue[0], charset) + "=" + URLEncoder.encode(keyvalue[1], charset);
			}
		}

		request.setRequestHeader("Cookie", cookie.isEmpty() ? null : cookie);
		return action.execute();
	}

	public void initialize(BeanContainer beanContainer) {
		Map<String, ?> beans = beanContainer.find(StringConverter.class);
		for (Object bean : beans.values()) {
			converters.add((StringConverter<?>) bean);
		}
	}

}
