package org.qfox.jestful.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.qfox.jestful.commons.Disposition;
import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.Multibody;
import org.qfox.jestful.commons.Multihead;
import org.qfox.jestful.commons.io.MultipartInputStream;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.RequestDeserializer;

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
public class MultipartRequestDeserializer implements RequestDeserializer, Initialable {
	private final Map<MediaType, RequestDeserializer> map = new HashMap<MediaType, RequestDeserializer>();

	public String getContentType() {
		return "multipart/form-data";
	}

	public void deserialize(Action action, MediaType mediaType, InputStream in) throws IOException {
		String boundary = mediaType.getParameters().get("boundary");
		List<Multipart> multiparts = new ArrayList<Multipart>();
		Parameter[] parameters = action.getParameters();
		MultipartInputStream mis = new MultipartInputStream(in, boundary);
		Multihead multihead = null;
		while ((multihead = mis.getNextMultihead()) != null) {
			Disposition disposition = multihead.getDisposition();
			MediaType type = multihead.getType();
			String name = disposition.getName();
			if (disposition.getFilename() != null) {
				Multibody multibody = new Multibody(mis);
				Multipart multipart = new Multipart(multihead, multibody);
				multiparts.add(multipart);
				for (Parameter parameter : parameters) {
					if (parameter.getName().equals(name) == false || parameter.getPosition() != Position.BODY) {
						continue;
					}
					if (type == null) {
						deserialize(action, parameter, multihead, mis);
						break;
					}
					if (map.containsKey(type)) {
						RequestDeserializer deserializer = map.get(type);
						deserializer.deserialize(action, parameter, multihead, mis);
						break;
					}
					if (parameter.getKlass().isInstance(multihead)) {
						parameter.setValue(multihead.clone());
						break;
					}
					if (parameter.getKlass().isInstance(multibody)) {
						parameter.setValue(multibody.clone());
						break;
					}
					if (parameter.getKlass().isInstance(multipart)) {
						parameter.setValue(multipart.clone());
						break;
					}
					break;
				}
			} else {
				for (Parameter parameter : parameters) {
					if (parameter.getName().equals(name) == false || parameter.getPosition() != Position.BODY) {
						continue;
					}
					if (type == null) {
						deserialize(action, parameter, multihead, mis);
						break;
					}
					if (map.containsKey(type)) {
						RequestDeserializer deserializer = map.get(type);
						deserializer.deserialize(action, parameter, multihead, mis);
						break;
					}
					if (parameter.getKlass().isInstance(multihead)) {
						parameter.setValue(multihead);
						break;
					}
					Multibody multibody = new Multibody(mis);
					if (parameter.getKlass().isInstance(multibody)) {
						parameter.setValue(multibody);
						break;
					}
					Multipart multipart = new Multipart(multihead, multibody);
					if (parameter.getKlass().isInstance(multipart)) {
						parameter.setValue(multipart);
						break;
					}
					break;
				}
			}
		}
		JestfulServletRequest jestfulServletRequest = (JestfulServletRequest) action.getRequest();
		jestfulServletRequest.setMultiparts(multiparts);
		mis.close();
	}

	public void deserialize(Action action, Parameter parameter, Multihead multihead, InputStream in) throws IOException {

	}

	public void initialize(BeanContainer beanContainer) {
		Collection<RequestDeserializer> deserializers = beanContainer.find(RequestDeserializer.class).values();
		for (RequestDeserializer deserializer : deserializers) {
			String contentType = deserializer.getContentType();
			MediaType mediaType = MediaType.valueOf(contentType);
			map.put(mediaType, deserializer);
		}
	}

}
