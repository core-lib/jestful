package org.qfox.jestful.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.qfox.jestful.commons.Disposition;
import org.qfox.jestful.commons.Multihead;
import org.qfox.jestful.commons.io.MultipartOutputStream;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.RequestSerializer;
import org.qfox.jestful.core.converter.StringConverter;

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
 * @date 2016年5月4日 下午4:26:25
 *
 * @since 1.0.0
 */
public class URLEncodedRequestSerializer implements RequestSerializer, Initialable {
	private final List<StringConverter<?>> converters = new ArrayList<StringConverter<?>>();
	private final String contentType = "application/x-www-form-urlencoded";

	public String getContentType() {
		return contentType;
	}

	public boolean supports(Action action) {
		List<Parameter> bodies = action.getParameters().all(Position.BODY);
		for (Parameter body : bodies) {
			if (supports(body) == false) {
				return false;
			}
		}
		return true;
	}

	public boolean supports(Parameter parameter) {
		for (StringConverter<?> converter : converters) {
			if (converter.support(parameter.getKlass())) {
				return true;
			}
		}
		return false;
	}

	public void serialize(Action action, OutputStream out) throws IOException {
		String charset = action.getCharset();
		List<Parameter> bodies = action.getParameters().all(Position.BODY);
		StringBuilder builder = new StringBuilder();
		for (Parameter body : bodies) {
			if (body.getValue() == null) {
				continue;
			}
			for (StringConverter<?> converter : converters) {
				if (converter.support(body.getKlass())) {
					@SuppressWarnings("unchecked")
					StringConverter<Object> stringConverter = (StringConverter<Object>) converter;
					String name = body.getName();
					String value = stringConverter.convert(body.getKlass(), body.getValue());
					if (builder.length() > 0) {
						builder.append("&");
					}
					builder.append(URLEncoder.encode(name, charset));
					builder.append("=");
					builder.append(URLEncoder.encode(value, charset));
					break;
				}
			}
		}
		if (builder.length() > 0) {
			action.getRequest().setRequestHeader("Content-Type", contentType);
			out.write(builder.toString().getBytes());
		}
	}

	public void serialize(Action action, Parameter parameter, MultipartOutputStream out) throws IOException {
		if (parameter.getValue() == null) {
			return;
		}
		String charset = action.getCharset();
		for (StringConverter<?> converter : converters) {
			if (converter.support(parameter.getKlass())) {
				@SuppressWarnings("unchecked")
				StringConverter<Object> stringConverter = (StringConverter<Object>) converter;
				String name = parameter.getName();
				String value = stringConverter.convert(parameter.getKlass(), parameter.getValue());
				Disposition disposition = Disposition.valueOf("form-data; name=\"" + name + "\"");
				Multihead multihead = new Multihead(disposition, null);
				out.setNextMultihead(multihead);
				out.write(URLEncoder.encode(value, charset).getBytes());
			}
		}
	}

	public void initialize(BeanContainer beanContainer) {
		Map<String, ?> beans = beanContainer.find(StringConverter.class);
		for (Object bean : beans.values()) {
			converters.add((StringConverter<?>) bean);
		}
	}

}
