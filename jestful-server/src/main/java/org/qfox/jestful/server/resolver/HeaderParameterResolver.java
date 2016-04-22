package org.qfox.jestful.server.resolver;

import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.Map;

import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.server.converter.ConversionProvider;

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
		Parameter[] parameters = action.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			String name = parameter.getName();
			if (parameter.getPosition() != Position.HEADER || map.containsKey(name) == false) {
				continue;
			}
			Type type = parameter.getType();
			Object value = headerConversionProvider.convert(name, type, map);
			parameter.setValue(value);
		}
		return action.execute();
	}

	public void initialize(BeanContainer beanContainer) {
		headerConversionProvider = beanContainer.get(ConversionProvider.class);
	}

}
