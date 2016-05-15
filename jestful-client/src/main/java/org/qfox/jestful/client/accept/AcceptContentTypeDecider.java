package org.qfox.jestful.client.accept;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.core.Accepts;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.formatting.ResponseDeserializer;

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
 * @date 2016年5月5日 下午12:29:59
 *
 * @since 1.0.0
 */
public class AcceptContentTypeDecider implements Actor, Initialable {
	private final Map<MediaType, ResponseDeserializer> map = new HashMap<MediaType, ResponseDeserializer>();

	public Object react(Action action) throws Exception {
		Accepts produces = action.getProduces();
		Accepts supports = new Accepts(map.keySet());
		if (produces.isEmpty() == false) {
			supports.retainAll(produces);
		}
		Request request = action.getRequest();
		String version = action.getMapping().getVersion();
		request.setRequestHeader("Accept", supports.toString(version));
		return action.execute();
	}

	public void initialize(BeanContainer beanContainer) {
		Collection<ResponseDeserializer> deserializers = beanContainer.find(ResponseDeserializer.class).values();
		for (ResponseDeserializer deserializer : deserializers) {
			String contentType = deserializer.getContentType();
			MediaType mediaType = MediaType.valueOf(contentType);
			map.put(mediaType, deserializer);
		}
	}

}
