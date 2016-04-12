package org.qfox.jestful.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.qfox.jestful.commons.Disposition;
import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.Multipart;
import org.qfox.jestful.commons.io.MultipartInputStream;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.RequestDeserializer;
import org.qfox.jestful.core.annotation.Argument.Position;
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
 * @date 2016年4月9日 下午6:28:28
 *
 * @since 1.0.0
 */
public class MultipartRequestDeserializer implements RequestDeserializer, ApplicationContextAware {
	private final Map<MediaType, RequestDeserializer> map = new HashMap<MediaType, RequestDeserializer>();

	public String getContentType() {
		return "multipart/form-data";
	}

	public void deserialize(Action action, MediaType mediaType, InputStream in) throws IOException {
		String boundary = mediaType.getParameters().get("boundary");
		Parameter[] parameters = action.getParameters();
		MultipartInputStream mis = new MultipartInputStream(in, boundary);
		Multipart multipart = null;
		while ((multipart = mis.getNextMultipart()) != null) {
			Disposition disposition = multipart.getDisposition();
			MediaType type = multipart.getType();
			String name = disposition.getName();
			for (Parameter parameter : parameters) {
				if (parameter.getName().equals(name) == false || parameter.getPosition() != Position.BODY) {
					continue;
				}
				if (type == null) {
					deserialize(action, parameter, multipart, mis);
				}
				if (map.containsKey(type)) {
					RequestDeserializer deserializer = map.get(type);
					deserializer.deserialize(action, parameter, multipart, mis);
				}
				break;
			}
		}
		mis.close();
	}

	public void deserialize(Action action, Parameter parameter, Multipart multipart, InputStream in) throws IOException {

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
