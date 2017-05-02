package org.qfox.jestful.server.resolver;

import org.qfox.jestful.commons.MapKit;
import org.qfox.jestful.core.*;
import org.qfox.jestful.server.converter.ConversionProvider;

import java.util.Map;

/**
 * Created by yangchangpei on 17/5/2.
 */
public class QueryResolver implements Resolver, Initialable {
    private ConversionProvider conversionProvider;

    @Override
    public boolean supports(Action action, Parameter parameter) {
        return parameter.getPosition() == Position.QUERY;
    }

    @Override
    public void resolve(Action action, Parameter parameter) throws Exception {
        if (parameter.getValue() != null) return;

        String query = action.getQuery();
        if (query == null || query.length() == 0) return;
        String charset = action.getQueryEncodeCharset();
        Map<String, String[]> map = MapKit.fromQueryString(query, charset);

        boolean decoded = parameter.isCoding() == false || (parameter.isCoding() && parameter.isDecoded());
        Object value = conversionProvider.convert(parameter.getName(), parameter.getType(), decoded, charset, map);
        parameter.setValue(value);
    }

    public void initialize(BeanContainer beanContainer) {
        conversionProvider = beanContainer.get(ConversionProvider.class);
    }

}
