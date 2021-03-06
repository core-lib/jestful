package org.qfox.jestful.client.handler;

import org.qfox.jestful.client.annotation.Queries;
import org.qfox.jestful.client.exception.IllegalQueryException;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.Resource;

import java.net.URLEncoder;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月28日 下午6:57:09
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
                String k = keyvalue[0];
                String v = keyvalue[1];
                k = URLEncoder.encode(k, charset);
                if (!queries.encoded()) {
                    v = URLEncoder.encode(v, charset);
                }
                query += (query.length() == 0 ? "" : "&") + k + "=" + v;
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
                String k = keyvalue[0];
                String v = keyvalue[1];
                if (!queries.encoded()) {
                    k = URLEncoder.encode(k, charset);
                    v = URLEncoder.encode(v, charset);
                }
                query += (query.length() == 0 ? "" : "&") + k + "=" + v;
            }
        }

        action.setQuery(query == null || query.length() == 0 ? null : query);
        return action.execute();
    }

}
