package org.qfox.jestful.client.handler;

import org.qfox.jestful.client.annotation.Headers;
import org.qfox.jestful.client.exception.IllegalHeaderException;
import org.qfox.jestful.core.*;

import java.net.URLEncoder;

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
 * @date 2016年4月28日 下午8:08:09
 * @since 1.0.0
 */
public class HeadersAnnotationHandler implements Actor {

	public Object react(Action action) throws Exception {
		Request request = action.getRequest();
		String charset = action.getHeaderEncodeCharset();

		Resource resource = action.getResource();
		if (resource.isAnnotationPresent(Headers.class)) {
			Headers headers = resource.getAnnotation(Headers.class);
			String[] values = headers.value();
			for (String value : values) {
				String[] keyvalue = value.split(":");
				if (keyvalue.length != 2) {
					throw new IllegalHeaderException(value + " is not a key-value pair like key: value", value);
				}
				String k = keyvalue[0];
				String v = keyvalue[1];
				k = URLEncoder.encode(k, charset);
				if (!headers.encoded()) {
					v = URLEncoder.encode(v, charset);
				}
				request.setRequestHeader(k, v);
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
				String k = keyvalue[0];
				String v = keyvalue[1];
				if (!headers.encoded()) {
					k = URLEncoder.encode(k, charset);
					v = URLEncoder.encode(v, charset);
				}
				request.setRequestHeader(k, v);
			}
		}

		return action.execute();
	}
}
