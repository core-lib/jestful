package org.qfox.jestful.client.handler;

import java.net.URLEncoder;

import org.qfox.jestful.client.annotation.Queries;
import org.qfox.jestful.client.exception.IllegalQueryException;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.Resource;

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
 * @date 2016年4月28日 下午6:57:09
 *
 * @since 1.0.0
 */
public class QueriesAnnotationHandler implements Actor {

	public Object react(Action action) throws Exception {
		String query = action.getQuery();
		query = query != null ? query : "";
		String charset = action.getQueryEncodeCharset();

		Resource resource = action.getResource();
		if (resource.isAnnotationPresent(Queries.class)) {
			Queries queries = resource.getAnnotation(Queries.class);
			String[] values = queries.value();
			for (String value : values) {
				String[] keyvalue = value.split("=");
				if (keyvalue.length != 2) {
					throw new IllegalQueryException(value + " is not a key-value pair like key=value", value);
				}
				query += (query.isEmpty() ? "" : "&") + URLEncoder.encode(keyvalue[0], charset) + "=" + URLEncoder.encode(keyvalue[1], charset);
			}
		}

		Mapping mapping = action.getMapping();
		if (mapping.isAnnotationPresent(Queries.class)) {
			Queries queries = mapping.getAnnotation(Queries.class);
			String[] values = queries.value();
			for (String value : values) {
				String[] keyvalue = value.split("=");
				if (keyvalue.length != 2) {
					throw new IllegalQueryException(value + " is not a key-value pair like key=value", value);
				}
				query += (query.isEmpty() ? "" : "&") + URLEncoder.encode(keyvalue[0], charset) + "=" + URLEncoder.encode(keyvalue[1], charset);
			}
		}

		action.setQuery(query == null || query.isEmpty() ? null : query);
		return action.execute();
	}

}
