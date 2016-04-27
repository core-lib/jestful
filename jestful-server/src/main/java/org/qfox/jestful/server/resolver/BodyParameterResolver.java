package org.qfox.jestful.server.resolver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.RequestDeserializer;
import org.qfox.jestful.core.Restful;
import org.qfox.jestful.server.exception.UnsupportedTypeException;

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
 * @date 2016年4月8日 下午3:42:21
 *
 * @since 1.0.0
 */
public class BodyParameterResolver implements Actor, Initialable {
	private final Map<MediaType, RequestDeserializer> map = new HashMap<MediaType, RequestDeserializer>();

	public Object react(Action action) throws Exception {
		Restful restful = action.getRestful();
		if (restful.isAcceptBody() == false) {
			return action.execute();
		}
		Request request = action.getRequest();
		String contentType = request.getRequestHeader("Content-Type");
		MediaType mediaType = MediaType.valueOf(contentType);
		Set<MediaType> consumes = action.getConsumes();
		if (map.containsKey(mediaType) && (consumes.isEmpty() || consumes.contains(mediaType))) {
			RequestDeserializer deserializer = map.get(mediaType);
			deserializer.deserialize(action, mediaType, request.getRequestInputStream());
		} else {
			String URI = action.getURI();
			String method = action.getRestful().getMethod();
			throw new UnsupportedTypeException(URI, method, mediaType, consumes.isEmpty() ? map.keySet() : consumes);
		}
		return action.execute();
	}

	public void initialize(BeanContainer beanContainer) {
		Collection<RequestDeserializer> deserializers = beanContainer.find(RequestDeserializer.class).values();
		for (RequestDeserializer deserializer : deserializers) {
			String contentType = deserializer.getContentType();
			MediaType mediaType = MediaType.valueOf(contentType);
			map.put(mediaType, deserializer);
		}
	}

}
