package org.qfox.jestful.server.render;

import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Restful;
import org.qfox.jestful.core.Result;
import org.qfox.jestful.core.formatting.ResponseSerializer;

/**
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月9日 下午3:46:30
 *
 * @since 1.0.0
 */
public class DefaultResultRender implements Actor, Initialable {
	private final Map<MediaType, ResponseSerializer> map = new HashMap<MediaType, ResponseSerializer>();

	public Object react(Action action) throws Exception {
		Restful restful = action.getRestful();
		Result result = action.getResult();
		// 忽略没有回应体和声明void返回值的方法
		if (restful.isReturnBody() == false || result.getKlass() == Void.TYPE) {
			return action.execute();
		}

		Object value = action.execute();

		if (result.isRendered()) {
			return value;
		}

		Response response = action.getResponse();
		String charset = response.getResponseHeader("Content-Charset");
		String contentType = response.getResponseHeader("Content-Type");
		MediaType mediaType = MediaType.valueOf(contentType);
		ResponseSerializer serializer = map.get(mediaType);
		OutputStream out = response.getResponseOutputStream();
		serializer.serialize(action, mediaType, charset, out);
		out.flush();

		return value;
	}

	public void initialize(BeanContainer beanContainer) {
		Collection<ResponseSerializer> serializers = beanContainer.find(ResponseSerializer.class).values();
		for (ResponseSerializer serializer : serializers) {
			String contentType = serializer.getContentType();
			MediaType mediaType = MediaType.valueOf(contentType);
			map.put(mediaType, serializer);
		}
	}

}
