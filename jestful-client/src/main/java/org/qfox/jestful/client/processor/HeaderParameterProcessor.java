package org.qfox.jestful.client.processor;

import java.net.URLEncoder;
import java.util.List;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.converter.StringConversion;

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
 * @date 2016年4月28日 下午7:59:19
 *
 * @since 1.0.0
 */
public class HeaderParameterProcessor implements Actor, Initialable {
	private StringConversion headerStringConversion;

	public Object react(Action action) throws Exception {
		Request request = action.getRequest();
		String charset = action.getHeaderEncoding();
		List<Parameter> parameters = action.getParameters().all(Position.HEADER);
		for (Parameter parameter : parameters) {
			String name = parameter.getName();
			String[] values = headerStringConversion.convert(parameter);
			if (values == null) {
				continue;
			}
			for (int i = 0; values != null && i < values.length; i++) {
				String value = values[i];
				String regex = parameter.getRegex();
				if (regex != null && value.matches(regex) == false) {
					throw new IllegalArgumentException("converted value " + value + " does not matches regex " + regex);
				}
				values[i] = URLEncoder.encode(values[i], charset);
			}
			request.setRequestHeaders(URLEncoder.encode(name, charset), values);
		}
		return action.execute();
	}

	public void initialize(BeanContainer beanContainer) {
		this.headerStringConversion = beanContainer.get(StringConversion.class);
	}

	public StringConversion getHeaderStringConversion() {
		return headerStringConversion;
	}

	public void setHeaderStringConversion(StringConversion headerStringConversion) {
		this.headerStringConversion = headerStringConversion;
	}

}
