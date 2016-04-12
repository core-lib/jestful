package org.qfox.jestful.server.resolver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.RequestDeserializer;
import org.qfox.jestful.core.annotation.Command;
import org.qfox.jestful.server.exception.UnsupportedTypeException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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
public class BodyParameterResolver implements Actor, ApplicationContextAware {
	private final Map<MediaType, RequestDeserializer> map = new HashMap<MediaType, RequestDeserializer>();

	public Object react(Action action) throws Exception {
		Command command = action.getCommand();
		if (command.hasRequestBody() == false) {
			return action.execute();
		}
		Request request = action.getRequest();
		String contentType = request.getHeader("Content-Type");
		MediaType mediaType = MediaType.valueOf(contentType);
		Set<MediaType> consumes = action.getConsumes();
		if (map.containsKey(mediaType) && (consumes.isEmpty() || consumes.contains(mediaType))) {
			RequestDeserializer deserializer = map.get(mediaType);
			deserializer.deserialize(action, mediaType, request.getInputStream());
		} else {
			throw new UnsupportedTypeException(mediaType, consumes.isEmpty() ? map.keySet() : consumes);
		}
		return action.execute();
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Collection<RequestDeserializer> deserializers = applicationContext.getBeansOfType(RequestDeserializer.class).values();
		for (RequestDeserializer deserializer : deserializers) {
			String contentType = deserializer.getContentType();
			MediaType mediaType = MediaType.valueOf(contentType);
			map.put(mediaType, deserializer);
		}
	}

}
