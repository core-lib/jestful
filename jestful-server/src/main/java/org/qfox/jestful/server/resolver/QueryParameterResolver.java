package org.qfox.jestful.server.resolver;

import org.qfox.jestful.commons.Utils;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.JestfulIOException;
import org.qfox.jestful.server.converter.ConversionException;
import org.qfox.jestful.server.converter.ConversionProvider;
import org.qfox.jestful.server.converter.IncompatibleConversionException;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class QueryParameterResolver implements Actor, Initialable {
	private ConversionProvider queryConversionProvider;

	public Object react(Action action) throws Exception {
		String query = action.getQuery();
		if (query == null || query.length() == 0) {
			return action.execute();
		}
		String charset = action.getQueryEncodeCharset();
		String[] pairs = query.split("&+");
		Map<String, String[]> map = new HashMap<String, String[]>();
		for (String pair : pairs) {
			String[] keyvalue = pair.split("=+");
			String key = URLDecoder.decode(keyvalue[0], charset);
			String value = keyvalue.length > 1 ? URLDecoder.decode(keyvalue[1], charset) : "";
			if (map.containsKey(key) == false) {
				map.put(key, new String[0]);
			}
			String[] values = map.get(key);
			values = Utils.copyOf(values, values.length + 1);
			values[values.length - 1] = value;
			map.put(key, values);
		}
		List<Parameter> parameters = action.getParameters().all(Position.QUERY);
		for (Parameter parameter : parameters) {
			if (parameter.getValue() != null) {
				continue;
			}
			try {
				Object value = queryConversionProvider.convert(parameter.getName(), parameter.getType(), map);
				parameter.setValue(value);
			} catch (IncompatibleConversionException e) {
				throw new JestfulIOException(e);
			} catch (ConversionException e) {
				continue;
			}
		}
		return action.execute();
	}

	public void initialize(BeanContainer beanContainer) {
		queryConversionProvider = beanContainer.get(ConversionProvider.class);
	}

	public ConversionProvider getQueryConversionProvider() {
		return queryConversionProvider;
	}

	public void setQueryConversionProvider(ConversionProvider queryConversionProvider) {
		this.queryConversionProvider = queryConversionProvider;
	}

}
