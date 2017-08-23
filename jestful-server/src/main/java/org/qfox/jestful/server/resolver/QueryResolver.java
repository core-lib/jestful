package org.qfox.jestful.server.resolver;

import org.qfox.jestful.core.*;
import org.qfox.jestful.server.JestfulServletRequest;
import org.qfox.jestful.server.converter.ConversionProvider;
import org.qfox.jestful.server.formatting.URLEncodedServletRequest;

import java.util.Map;

/**
 * Created by yangchangpei on 17/5/2.
 */
public class QueryResolver implements Resolver, Initialable {
    private ConversionProvider conversionProvider;

    @Override
    public boolean supports(Action action, Parameter parameter) {
        return parameter.getPosition() == Position.QUERY && parameter.getValue() == null;
    }

    @Override
    public void resolve(Action action, Parameter parameter) throws Exception {
        String charset = action.getQueryEncodeCharset();
        Map<String, String[]> map = action.getQueries();

        boolean decoded = !parameter.isCoding() || (parameter.isCoding() && parameter.isDecoded());
        Object value = conversionProvider.convert(parameter.getName(), parameter.getType(), decoded, charset, map);
        parameter.setValue(value);

        Request oldRequest = action.getRequest();
        Request newRequest = new URLEncodedServletRequest((JestfulServletRequest) oldRequest, map);
        action.setRequest(newRequest);
    }

    public void initialize(BeanContainer beanContainer) {
        conversionProvider = beanContainer.get(ConversionProvider.class);
    }

}
