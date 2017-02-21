package org.qfox.jestful.server.formatting;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Disposition;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Multibody;
import org.qfox.jestful.core.Multihead;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.exception.JestfulIOException;
import org.qfox.jestful.core.formatting.RequestDeserializer;
import org.qfox.jestful.core.io.IOUtils;
import org.qfox.jestful.core.io.MultipartInputStream;
import org.qfox.jestful.server.JestfulServletRequest;
import org.qfox.jestful.server.converter.ConversionException;
import org.qfox.jestful.server.converter.ConversionProvider;
import org.qfox.jestful.server.converter.IncompatibleConversionException;

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
	private ConversionProvider multipartConversionProvider;

	public String getContentType() {
		return "multipart/form-data";
	}

	public void deserialize(Action action, MediaType mediaType, String charset, InputStream in) throws IOException {
		String boundary = mediaType.getParameters().get("boundary");
		List<Multipart> multiparts = new ArrayList<Multipart>();
		Map<String, String[]> fields = new HashMap<String, String[]>();
		List<Parameter> parameters = action.getParameters().all(Position.BODY);
		MultipartInputStream mis = new MultipartInputStream(in, boundary);
		Multihead multihead = null;
		while ((multihead = mis.getNextMultihead()) != null) {
			Disposition disposition = multihead.getDisposition();
			MediaType type = multihead.getType();
			String enc = charset;
			if (type != null && type.getCharset() != null) {
				enc = type.getCharset();
			}
			String name = disposition.getName();
			if (disposition.getFilename() != null) {
				Multibody multibody = new Multibody(mis);
				Multipart multipart = new Multipart(multihead, multibody);
				multiparts.add(multipart);
				for (Parameter parameter : parameters) {
					if (parameter.getName().equals(name) == false) {
						continue;
					}
					if (type == null) {
						deserialize(action, parameter, multihead, enc, mis);
						break;
					}
					if (map.containsKey(type)) {
						RequestDeserializer deserializer = map.get(type);
						deserializer.deserialize(action, parameter, multihead, enc, mis);
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
			} else if (type != null) {
				for (Parameter parameter : parameters) {
					if (parameter.getName().equals(name) == false) {
						continue;
					}
					if (map.containsKey(type)) {
						RequestDeserializer deserializer = map.get(type);
						deserializer.deserialize(action, parameter, multihead, enc, mis);
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
			} else {
				String value = IOUtils.toString(mis);
				String[] values = fields.get(name);
				if (values == null) {
					values = new String[] { value };
				} else {
					String[] array = new String[values.length + 1];
					System.arraycopy(values, 0, array, 0, values.length);
					array[values.length] = value;
					values = array;
				}
				fields.put(name, values);
			}
		}
		for (Parameter parameter : parameters) {
			if (parameter.getValue() != null) {
				continue;
			}
			try {
				boolean decoded = parameter.isCoding() == false || (parameter.isCoding() && parameter.isDecoded());
				Object value = multipartConversionProvider.convert(parameter.getName(), parameter.getType(), decoded, charset, fields);
				parameter.setValue(value);
			} catch (IncompatibleConversionException e) {
				throw new JestfulIOException(e);
			} catch (ConversionException e) {
				continue;
			}
		}
		JestfulServletRequest jestfulServletRequest = (JestfulServletRequest) action.getRequest();
		jestfulServletRequest.setMultiparts(multiparts);
		mis.close();
	}

	public void deserialize(Action action, Parameter parameter, Multihead multihead, String charset, InputStream in) throws IOException {

	}

	public void initialize(BeanContainer beanContainer) {
		Collection<RequestDeserializer> deserializers = beanContainer.find(RequestDeserializer.class).values();
		for (RequestDeserializer deserializer : deserializers) {
			String contentType = deserializer.getContentType();
			MediaType mediaType = MediaType.valueOf(contentType);
			map.put(mediaType, deserializer);
		}
		multipartConversionProvider = beanContainer.get(ConversionProvider.class);
	}

	public ConversionProvider getMultipartConversionProvider() {
		return multipartConversionProvider;
	}

	public void setMultipartConversionProvider(ConversionProvider multipartConversionProvider) {
		this.multipartConversionProvider = multipartConversionProvider;
	}

}
