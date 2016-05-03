package org.qfox.jestful.client;

import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.qfox.jestful.client.exception.NoSuchSerializerException;
import org.qfox.jestful.commons.Disposition;
import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.Multihead;
import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.commons.io.MultipartOutputStream;
import org.qfox.jestful.core.Accepts;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.Request;
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
 * @date 2016年4月30日 下午4:11:07
 *
 * @since 1.0.0
 */
public class BodyParameterProcessor implements Actor, Initialable {
	private final Map<MediaType, RequestSerializer> map = new HashMap<MediaType, RequestSerializer>();

	public Object react(Action action) throws Exception {
		List<Parameter> bodies = action.getParameters().all(Position.BODY);
		Accepts consumes = action.getConsumes();
		Request request = action.getRequest();
		OutputStream outputStream = request.getRequestOutputStream();
		if (bodies.size() > 1) {
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
				mos = new MultipartOutputStream(outputStream, boundary);
				for (Parameter body : bodies) {
					for (Entry<MediaType, RequestSerializer> entry : map.entrySet()) {
						MediaType mediaType = entry.getKey();
						RequestSerializer serializer = entry.getValue();
						if ((consumes.isEmpty() || consumes.contains(mediaType)) && serializer.supports(body)) {
							Map<String, String> header = new CaseInsensitiveMap<String, String>();
							Disposition disposition = Disposition.valueOf("form-data; name=\"" + body.getName() + "\"");
							header.put("Content-Disposition", disposition.toString());
							header.put("Content-Type", mediaType.toString());
							Multihead multihead = new Multihead(header);
							mos.setNextMultihead(multihead);
							serializer.serialize(action, body, multihead, mos);
						}
					}
					throw new NoSuchSerializerException(body, consumes, map.values());
				}
			} finally {
				mos.close();
			}
		} else if (bodies.size() == 1) {
			
		} else {
			
		}
		return null;
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
