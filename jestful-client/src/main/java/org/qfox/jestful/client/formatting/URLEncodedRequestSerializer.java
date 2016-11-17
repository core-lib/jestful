package org.qfox.jestful.client.formatting;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Disposition;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Multihead;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.converter.StringConversion;
import org.qfox.jestful.core.formatting.RequestSerializer;
import org.qfox.jestful.core.io.MultipartOutputStream;

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
	private StringConversion urlStringConversion;
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
		return urlStringConversion.supports(parameter);
	}

	public void serialize(Action action, String charset, OutputStream out) throws IOException {
		List<Parameter> bodies = action.getParameters().all(Position.BODY);
		StringBuilder builder = new StringBuilder();
		for (Parameter body : bodies) {
			if (body.getValue() == null) {
				continue;
			}
			String name = body.getName();
			String[] values = urlStringConversion.convert(body);
			for (int i = 0; values != null && i < values.length; i++) {
				String value = values[i];
				if (builder.length() > 0) {
					builder.append("&");
				}
				builder.append(URLEncoder.encode(name, charset));
				builder.append("=");
				builder.append(URLEncoder.encode(value, charset));
			}
		}
		if (builder.length() > 0) {
			action.getRequest().setContentType(contentType);
			out.write(builder.toString().getBytes());
		}
	}

	public void serialize(Action action, Parameter parameter, String charset, MultipartOutputStream out) throws IOException {
		if (parameter.getValue() == null) {
			return;
		}
		String name = parameter.getName();
		String[] values = urlStringConversion.convert(parameter);
		for (int i = 0; values != null && i < values.length; i++) {
			String value = values[i];
			Disposition disposition = Disposition.valueOf("form-data; name=\"" + name + "\"");
			Multihead multihead = new Multihead(disposition, null);
			out.setNextMultihead(multihead);
			out.write(URLEncoder.encode(value, charset).getBytes());
		}
	}

	public void initialize(BeanContainer beanContainer) {
		this.urlStringConversion = beanContainer.get(StringConversion.class);
	}

	public StringConversion getUrlStringConversion() {
		return urlStringConversion;
	}

	public void setUrlStringConversion(StringConversion urlStringConversion) {
		this.urlStringConversion = urlStringConversion;
	}

}
