package org.qfox.jestful.server.resolver;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
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
public class QueryParameterResolver implements Actor, Initialable {
	private ConversionProvider queryConversionProvider;

	public Object react(Action action) throws Exception {
		String query = action.getQuery();
		if (query == null || query.isEmpty()) {
			return action.execute();
		}
		String charset = action.getCharset();
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
			values = Arrays.copyOf(values, values.length + 1);
			values[values.length - 1] = value;
			map.put(key, values);
		}
		Parameter[] parameters = action.getParameters();
		for (Parameter parameter : parameters) {
			if (parameter.getPosition() != Position.QUERY || parameter.getValue() != null) {
				continue;
			}
			try {
				Object value = queryConversionProvider.convert(parameter.getName(), parameter.getType(), map);
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
		queryConversionProvider = beanContainer.get(ConversionProvider.class);
	}

}
