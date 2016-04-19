package org.qfox.jestful.server.render;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.ResponseSerializer;
import org.qfox.jestful.core.Restful;
import org.qfox.jestful.core.Result;
import org.qfox.jestful.server.exception.NotAcceptableStatusException;
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
 * @date 2016年4月9日 下午3:46:30
 *
 * @since 1.0.0
 */
public class DefaultResultRender implements Actor, ApplicationContextAware {
	private final Map<MediaType, ResponseSerializer> map = new HashMap<MediaType, ResponseSerializer>();

	public Object react(Action action) throws Exception {
		Restful restful = action.getRestful();
		Result result = action.getResult();
		// 忽略没有回应体和声明void返回值的方法
		if (restful.isReturnBody() == false || result.getKlass() == Void.TYPE) {
			return action.execute();
		}

		MediaType mediaType = getMediaType(action);

		Object value = action.execute();

		Response response = action.getResponse();
		response.setResponseHeader("Content-Type", mediaType.getName());
		ResponseSerializer serializer = map.get(mediaType);
		serializer.serialize(action, mediaType, response.getResponseOutputStream());

		return value;
	}

	private MediaType getMediaType(Action action) throws NotAcceptableStatusException {
		Request request = action.getRequest();
		String accept = request.getRequestHeader("Accept");
		Set<MediaType> accepts = new TreeSet<MediaType>();
		String[] mediaTypes = accept != null ? accept.split(",") : new String[0];
		for (String contentType : mediaTypes) {
			MediaType mediaType = MediaType.valueOf(contentType);
			accepts.add(mediaType);
		}

		Set<MediaType> produces = action.getProduces();
		MediaType mediaType = null;
		for (MediaType type : accepts) {
			if (map.containsKey(type) && (produces.isEmpty() || produces.contains(type))) {
				mediaType = type;
				break;
			}
			if (type.getType().equals("*")) {
				Iterator<MediaType> iterator = map.keySet().iterator();
				while (iterator.hasNext() && mediaType == null) {
					MediaType next = iterator.next();
					if (next.getSubtype().equalsIgnoreCase(type.getSubtype()) && (produces.isEmpty() || produces.contains(next))) {
						mediaType = next;
						break;
					}
				}
				if (mediaType != null) {
					break;
				}
			}
			if (type.getSubtype().equals("*")) {
				Iterator<MediaType> iterator = map.keySet().iterator();
				while (iterator.hasNext() && mediaType == null) {
					MediaType next = iterator.next();
					if (next.getType().equalsIgnoreCase(type.getType()) && (produces.isEmpty() || produces.contains(next))) {
						mediaType = next;
						break;
					}
				}
				if (mediaType != null) {
					break;
				}
			}
			if (type.getType().equals("*") && type.getSubtype().equals("*")) {
				Iterator<MediaType> iterator = map.keySet().iterator();
				while (iterator.hasNext() && mediaType == null) {
					MediaType next = iterator.next();
					if (produces.isEmpty() || produces.contains(next)) {
						mediaType = next;
						break;
					}
				}
				if (mediaType != null) {
					break;
				}
			}
		}
		if (mediaType == null) {
			throw new NotAcceptableStatusException(accepts, produces.isEmpty() ? map.keySet() : produces);
		}
		return mediaType;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Collection<ResponseSerializer> serializers = applicationContext.getBeansOfType(ResponseSerializer.class).values();
		for (ResponseSerializer serializer : serializers) {
			String contentType = serializer.getContentType();
			MediaType mediaType = MediaType.valueOf(contentType);
			map.put(mediaType, serializer);
		}
	}

}
