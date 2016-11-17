package org.qfox.jestful.client.formatting;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.qfox.jestful.client.exception.NoSuchSerializerException;
import org.qfox.jestful.core.Accepts;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Disposition;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Multihead;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.exception.JestfulIOException;
import org.qfox.jestful.core.formatting.RequestSerializer;
import org.qfox.jestful.core.io.IOUtils;
import org.qfox.jestful.core.io.MultipartOutputStream;

import eu.medsea.mimeutil.MimeUtil;

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
	private final String contentType = "multipart/form-data";

	public String getContentType() {
		return contentType;
	}

	public boolean supports(Action action) {
		List<Parameter> bodies = action.getParameters().all(Position.BODY);
		return bodies.size() == 0 ? true : bodies.size() == 1 ? supports(bodies.get(0)) : true;
	}

	public boolean supports(Parameter parameter) {
		return File.class.isAssignableFrom(parameter.getKlass());
	}

	public void serialize(Action action, String charset, OutputStream out) throws IOException {
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
		action.getRequest().setContentType(contentType + ";boundary=" + boundary);
		MultipartOutputStream mos = null;
		try {
			mos = new MultipartOutputStream(out, boundary);
			flag: for (Parameter body : bodies) {
				{
					MediaType mediaType = MediaType.valueOf(contentType);
					RequestSerializer serializer = this;
					if ((consumes.isEmpty() || consumes.contains(mediaType)) && serializer.supports(body)) {
						serializer.serialize(action, body, charset, mos);
						continue flag;
					}
				}
				for (Entry<MediaType, RequestSerializer> entry : map.entrySet()) {
					MediaType mediaType = entry.getKey();
					RequestSerializer serializer = entry.getValue();
					if ((consumes.isEmpty() || consumes.contains(mediaType)) && serializer.supports(body)) {
						serializer.serialize(action, body, charset, mos);
						continue flag;
					}
				}
				throw new NoSuchSerializerException(action, body, consumes, map.values());
			}
			mos.flush();
		} catch (NoSuchSerializerException e) {
			throw new JestfulIOException(e);
		} finally {
			mos.close(true);
		}
	}

	public void serialize(Action action, Parameter parameter, String charset, MultipartOutputStream out) throws IOException {
		File file = (File) parameter.getValue();
		String name = parameter.getName();
		if (file == null) {
			Disposition disposition = Disposition.valueOf("form-data; name=\"" + name + "\"");
			Multihead multihead = new Multihead(disposition, null);
			out.setNextMultihead(multihead);
		} else {
			String filename = file.getName();
			Disposition disposition = Disposition.valueOf("form-data; name=\"" + name + "\"; filename=\"" + filename + "\"");
			Collection<?> mediaTypes = MimeUtil.getMimeTypes(file);
			String mediaType = mediaTypes == null || mediaTypes.isEmpty() ? "application/octet-stream" : mediaTypes.toArray()[0].toString();
			MediaType type = MediaType.valueOf(mediaType);
			Multihead multihead = new Multihead(disposition, type);
			out.setNextMultihead(multihead);
			IOUtils.transfer(file, out);
		}
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
