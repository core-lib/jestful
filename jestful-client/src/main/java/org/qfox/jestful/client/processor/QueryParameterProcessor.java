package org.qfox.jestful.client.processor;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.converter.StringConversion;

import java.net.URLEncoder;
import java.util.List;

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
 * @date 2016年4月28日 下午5:49:14
 * @since 1.0.0
 */
public class QueryParameterProcessor implements Actor, Initialable {
    private StringConversion queryStringConversion;

    public Object react(Action action) throws Exception {
        String query = action.getQuery();
        query = query != null ? query : "";
        String charset = action.getQueryEncodeCharset();
        List<Parameter> parameters = action.getParameters().all(Position.QUERY);
        for (Parameter parameter : parameters) {
            String[] values = queryStringConversion.convert(parameter);
            for (int i = 0; values != null && i < values.length; i++) {
                String value = values[i];
                if (value == null) {
                    continue;
                }
                String regex = parameter.getRegex();
                if (regex != null && !value.matches(regex)) {
                    throw new IllegalArgumentException("converted value " + value + " does not matches regex " + regex);
                }
                String name = parameter.getName();
                name = URLEncoder.encode(name, charset);
                if (parameter.isCoding() && !parameter.isEncoded()) {
                    value = URLEncoder.encode(value, charset);
                }
                query += (query.length() == 0 ? "" : "&") + name + "=" + value;
            }
        }
        action.setQuery(query == null || query.length() == 0 ? null : query);
        return action.execute();
    }

    public void initialize(BeanContainer beanContainer) {
        this.queryStringConversion = beanContainer.get(StringConversion.class);
    }

    public StringConversion getQueryStringConversion() {
        return queryStringConversion;
    }

    public void setQueryStringConversion(StringConversion queryStringConversion) {
        this.queryStringConversion = queryStringConversion;
    }

}
