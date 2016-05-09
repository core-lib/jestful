package org.qfox.jestful.server.resolver;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.Request;
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
 * @date 2016年4月8日 下午12:08:44
 *
 * @since 1.0.0
 */
public class HeaderParameterResolver implements Actor, Initialable {
	private ConversionProvider headerConversionProvider;

	public Object react(Action action) throws Exception {
		Map<String, String[]> map = new CaseInsensitiveMap<String, String[]>();
		Request request = action.getRequest();
		String charset = action.getCharset();
		String[] keys = request.getHeaderKeys();
		for (String key : keys) {
			String[] values = request.getRequestHeaders(key);
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				values[i] = URLDecoder.decode(value, charset);
			}
			map.put(key, values);
		}
		List<Parameter> parameters = action.getParameters().all(Position.HEADER);
		for (Parameter parameter : parameters) {
			if (parameter.getValue() != null) {
				continue;
			}
			try {
				Object value = headerConversionProvider.convert(parameter.getName(), parameter.getType(), map);
				parameter.setValue(value);
			} catch (IncompatibleConversionException e) {
				throw new IOException(e);
			} catch (ConversionException e) {
				continue;
			}
		}
		return action.execute();
	}

	public void initialize(BeanContainer beanContainer) {
		headerConversionProvider = beanContainer.get(ConversionProvider.class);
	}

	public ConversionProvider getHeaderConversionProvider() {
		return headerConversionProvider;
	}

	public void setHeaderConversionProvider(ConversionProvider headerConversionProvider) {
		this.headerConversionProvider = headerConversionProvider;
	}

}
