package org.qfox.jestful.client.processor;

import org.qfox.jestful.client.formatting.URLEncodes;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.converter.StringConversion;

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
        String encode = URLEncodes.encode(charset, parameters, queryStringConversion);
        if (!encode.isEmpty()) action.setQuery(query.isEmpty() ? encode : query + "&" + encode);
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
