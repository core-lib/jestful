package org.qfox.jestful.client;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.qfox.jestful.client.exception.NoSuchSerializerException;
import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.io.MultipartOutputStream;
import org.qfox.jestful.core.Accepts;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.RequestSerializer;

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
 * @date 2016年5月4日 下午3:52:47
 *
 * @since 1.0.0
 */
public class MultipartRequestSerializer implements RequestSerializer, Initialable {
	private final Map<MediaType, RequestSerializer> map = new HashMap<MediaType, RequestSerializer>();

	public String getContentType() {
		return "multipart/form-data";
	}

	public boolean supports(Action action) {
		List<Parameter> bodies = action.getParameters().all(Position.BODY);
		return bodies.size() > 1;
	}

	public boolean supports(Parameter parameter) {
		return false;
	}

	public void serialize(Action action, OutputStream out) throws IOException {
		List<Parameter> bodies = action.getParameters().all(Position.BODY);
		Accepts consumes = action.getConsumes();
		String base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String nonce = "";
		Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < 16; i++) {
			int bound = base.length();
			int index = random.nextInt(bound);
			nonce += base.charAt(index);
		}
		String boundary = "----JestfulFormBoundary" + nonce;
		MultipartOutputStream mos = null;
		try {
			mos = new MultipartOutputStream(out, boundary);
			for (Parameter body : bodies) {
				for (Entry<MediaType, RequestSerializer> entry : map.entrySet()) {
					MediaType mediaType = entry.getKey();
					RequestSerializer serializer = entry.getValue();
					if ((consumes.isEmpty() || consumes.contains(mediaType)) && serializer.supports(body)) {
						serializer.serialize(action, body, mos);
					}
				}
				throw new NoSuchSerializerException(action, body, consumes, map.values());
			}
		} catch (NoSuchSerializerException e) {
			throw new IOException(e);
		} finally {
			mos.close();
		}
	}

	public void serialize(Action action, Parameter parameter, MultipartOutputStream out) throws IOException {
		throw new UnsupportedOperationException();
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
