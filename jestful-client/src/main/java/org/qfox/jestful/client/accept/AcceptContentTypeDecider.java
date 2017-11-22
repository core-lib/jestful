package org.qfox.jestful.client.accept;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.formatting.ResponseDeserializer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

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
 * @date 2016年5月5日 下午12:29:59
 * @since 1.0.0
 */
public class AcceptContentTypeDecider implements Actor, Initialable {
	private final Map<MediaType, ResponseDeserializer> map = new LinkedHashMap<MediaType, ResponseDeserializer>();

	public Object react(Action action) throws Exception {
		Accepts produces = action.getProduces();
		Accepts supports = new Accepts(map.keySet());
		if (!produces.isEmpty()) {
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
