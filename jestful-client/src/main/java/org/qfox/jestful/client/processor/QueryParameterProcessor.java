package org.qfox.jestful.client.processor;

import org.qfox.jestful.client.formatting.URLEncodes;
import org.qfox.jestful.commons.conversion.ConversionProvider;
import org.qfox.jestful.core.*;

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
    private ConversionProvider queryConversionProvider;

    public Object react(Action action) throws Exception {
        String query = action.getQuery();
        query = query != null ? query : "";
        String charset = action.getQueryEncodeCharset();
        List<Parameter> parameters = action.getParameters().all(Position.QUERY);
        String encode = URLEncodes.encode(charset, parameters, queryConversionProvider);
        if (!encode.isEmpty()) action.setQuery(query.isEmpty() ? encode : query + "&" + encode);
        return action.execute();
    }

    public void initialize(BeanContainer beanContainer) {
        this.queryConversionProvider = beanContainer.get(ConversionProvider.class);
    }

    public ConversionProvider getQueryConversionProvider() {
        return queryConversionProvider;
    }

    public void setQueryConversionProvider(ConversionProvider queryConversionProvider) {
        this.queryConversionProvider = queryConversionProvider;
    }

}
