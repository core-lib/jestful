package org.qfox.jestful.server.render;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.qfox.jestful.core.Accepts;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Charsets;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Restful;
import org.qfox.jestful.core.Result;
import org.qfox.jestful.core.exception.NoSuchCharsetException;
import org.qfox.jestful.core.formatting.ResponseSerializer;
import org.qfox.jestful.server.exception.NotAcceptableStatusException;

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
	private final Charsets charsets = new Charsets(Charset.availableCharsets().keySet().toArray(new String[0]));
	private final Map<MediaType, ResponseSerializer> map = new HashMap<MediaType, ResponseSerializer>();

	public Object react(Action action) throws Exception {
		Restful restful = action.getRestful();
		Result result = action.getResult();
		// 忽略没有回应体和声明void返回值的方法
		if (restful.isReturnBody() == false || result.getKlass() == Void.TYPE) {
			return action.execute();
		}

		String charset = getContentCharset(action);
		MediaType mediaType = getMediaType(action);

		Object value = action.execute();

		if (result.isRendered()) {
			return value;
		}

		Response response = action.getResponse();
		response.setResponseHeader("Content-Charset", charset);
		response.setResponseHeader("Content-Type", mediaType.getName());
		ResponseSerializer serializer = map.get(mediaType);
		serializer.serialize(action, mediaType, charset, response.getResponseOutputStream());

		return value;
	}

	private String getContentCharset(Action action) throws NoSuchCharsetException {
		String charset = null;
		Request request = action.getRequest();
		String accept = request.getRequestHeader("Accept-Charset");
		Charsets accepts = accept == null || accept.isEmpty() ? charsets.clone() : Charsets.valueOf(accept);
		Charsets options = action.getContentCharsets().clone();
		Charsets supports = charsets.clone();
		if ((accept == null || accept.isEmpty()) && options.isEmpty()) {
			charset = Charset.defaultCharset().name();
		} else if (options.isEmpty()) {
			accepts.retainAll(supports);
			if (accepts.isEmpty()) {
				throw new NoSuchCharsetException(Charsets.valueOf(accept), supports);
			}
			charset = accepts.first().getName();
		} else {
			options.retainAll(supports);
			if (options.isEmpty()) {
				throw new NoSuchCharsetException(action.getContentCharsets().clone(), supports);
			}
			charset = options.first().getName();
		}
		return charset;
	}

	private MediaType getMediaType(Action action) throws NotAcceptableStatusException {
		Request request = action.getRequest();
		String accept = request.getRequestHeader("Accept");

		Accepts accepts = accept == null || accept.isEmpty() ? new Accepts(map.keySet()) : Accepts.valueOf(accept);
		Accepts produces = action.getProduces();
		Accepts supports = new Accepts(map.keySet());

		for (MediaType mediaType : accepts) {
			if ((produces.isEmpty() || produces.contains(mediaType)) && (supports.contains(mediaType))) {
				return mediaType;
			}
			if (mediaType.isWildcard()) {
				if (produces.isEmpty()) {
					for (MediaType support : supports) {
						if (mediaType.matches(support)) {
							return support;
						}
					}
				} else {
					for (MediaType produce : produces) {
						if (mediaType.matches(produce) && supports.contains(produce)) {
							return produce;
						}
					}
				}
			}
		}

		String URI = action.getURI();
		String method = action.getRestful().getMethod();
		throw new NotAcceptableStatusException(URI, method, accepts, produces.isEmpty() ? supports : produces);
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
