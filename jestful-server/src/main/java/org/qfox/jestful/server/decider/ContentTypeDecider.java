package org.qfox.jestful.server.decider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.qfox.jestful.core.Accepts;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.formatting.ResponseSerializer;
import org.qfox.jestful.server.exception.NotAcceptableStatusException;

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
 * @date 2016年6月8日 上午10:47:37
 *
 * @since 1.0.0
 */
public class ContentTypeDecider implements Actor, Initialable {
	private final Map<MediaType, ResponseSerializer> map = new HashMap<MediaType, ResponseSerializer>();

	public Object react(Action action) throws Exception {
		Request request = action.getRequest();
		Response response = action.getResponse();
		String accept = request.getRequestHeader("Accept");

		Accepts accepts = accept == null || accept.length() == 0 ? new Accepts(map.keySet()) : Accepts.valueOf(accept);
		Accepts produces = action.getProduces();
		Accepts supports = new Accepts(map.keySet());

		MediaType contentType = null;

		for (MediaType mediaType : accepts) {
			if ((produces.isEmpty() || produces.contains(mediaType)) && (supports.contains(mediaType))) {
				contentType = mediaType;
				break;
			}
			if (mediaType.isWildcard()) {
				if (produces.isEmpty()) {
					for (MediaType support : supports) {
						if (mediaType.matches(support)) {
							contentType = support;
							break;
						}
					}
				} else {
					for (MediaType produce : produces) {
						if (mediaType.matches(produce) && supports.contains(produce)) {
							contentType = produce;
							break;
						}
					}
				}
			}
		}

		if (contentType != null) {
			response.setResponseHeader("Content-Type", contentType.getName());
		} else {
			String URI = action.getURI();
			String method = action.getRestful().getMethod();
			throw new NotAcceptableStatusException(URI, method, accepts, produces.isEmpty() ? supports : produces);
		}

		return action.execute();
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
