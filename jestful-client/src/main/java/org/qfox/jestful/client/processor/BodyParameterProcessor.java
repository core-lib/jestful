package org.qfox.jestful.client.processor;

import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.qfox.jestful.client.exception.NoSuchSerializerException;
import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.core.Accepts;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.RequestSerializer;
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
 * @date 2016年4月30日 下午4:11:07
 *
 * @since 1.0.0
 */
public class BodyParameterProcessor implements Actor, Initialable {
	private final Map<MediaType, RequestSerializer> map = new HashMap<MediaType, RequestSerializer>();

	public Object react(Action action) throws Exception {
		Restful restful = action.getRestful();
		if (restful.isAcceptBody() == false) {
			return action.execute();
		}
		List<Parameter> bodies = action.getParameters().all(Position.BODY);
		Accepts consumes = action.getConsumes();
		if (bodies.isEmpty()) {
			return action.execute();
		} else {
			for (Entry<MediaType, RequestSerializer> entry : map.entrySet()) {
				MediaType mediaType = entry.getKey();
				RequestSerializer serializer = entry.getValue();
				if ((consumes.isEmpty() || consumes.contains(mediaType)) && serializer.supports(action)) {
					Request request = action.getRequest();
					OutputStream out = request.getRequestOutputStream();
					serializer.serialize(action, out);
					return action.execute();
				}
			}
		}
		throw new NoSuchSerializerException(action, null, consumes, map.values());
	}

	public void initialize(BeanContainer beanContainer) {
		Collection<RequestSerializer> serializers = beanContainer.find(RequestSerializer.class).values();
		for (RequestSerializer serializer : serializers) {
			String contentType = serializer.getContentType();
			MediaType mediaType = MediaType.valueOf(contentType);
			map.put(mediaType, serializer);
		}
	}

}
