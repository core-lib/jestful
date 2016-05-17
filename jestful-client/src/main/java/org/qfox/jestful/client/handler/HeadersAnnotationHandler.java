package org.qfox.jestful.client.handler;

import java.net.URLEncoder;

import org.qfox.jestful.client.annotation.Headers;
import org.qfox.jestful.client.exception.IllegalHeaderException;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Resource;

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
 * @date 2016年4月28日 下午8:08:09
 *
 * @since 1.0.0
 */
public class HeadersAnnotationHandler implements Actor {

	public Object react(Action action) throws Exception {
		Request request = action.getRequest();
		String charset = action.getHeaderEncoding();

		Resource resource = action.getResource();
		if (resource.isAnnotationPresent(Headers.class)) {
			Headers headers = resource.getAnnotation(Headers.class);
			String[] values = headers.value();
			for (String value : values) {
				String[] keyvalue = value.split(":");
				if (keyvalue.length != 2) {
					throw new IllegalHeaderException(value + " is not a key-value pair like key: value", value);
				}
				request.setRequestHeader(URLEncoder.encode(keyvalue[0], charset), URLEncoder.encode(keyvalue[1], charset));
			}
		}

		Mapping mapping = action.getMapping();
		if (mapping.isAnnotationPresent(Headers.class)) {
			Headers headers = mapping.getAnnotation(Headers.class);
			String[] values = headers.value();
			for (String value : values) {
				String[] keyvalue = value.split(":");
				if (keyvalue.length != 2) {
					throw new IllegalHeaderException(value + " is not a key-value pair like key: value", value);
				}
				request.setRequestHeader(URLEncoder.encode(keyvalue[0], charset), URLEncoder.encode(keyvalue[1], charset));
			}
		}

		return action.execute();
	}
}
