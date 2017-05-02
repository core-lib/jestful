package org.qfox.jestful.server.resolver;

import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.*;
import org.qfox.jestful.server.converter.ConversionProvider;

import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by yangchangpei on 17/5/2.
 */
public class HeaderResolver implements Resolver {
    private ConversionProvider conversionProvider;

    @Override
    public boolean supports(Action action, Parameter parameter) {
        return parameter.getPosition() == Position.HEADER;
    }

    @Override
    public void resolve(Action action, Parameter parameter) throws Exception {
        if (parameter.getValue() != null) return;

        Map<String, String[]> map = new CaseInsensitiveMap<String, String[]>();
        Request request = action.getRequest();
        String charset = action.getHeaderEncodeCharset();
        String[] keys = request.getHeaderKeys();
        for (String key : keys) {
            String[] values = request.getRequestHeaders(key);
            map.put(URLDecoder.decode(key, charset), values);
        }

        boolean decoded = parameter.isCoding() == false || (parameter.isCoding() && parameter.isDecoded());
        Object value = conversionProvider.convert(parameter.getName(), parameter.getType(), decoded, charset, map);
        parameter.setValue(value);
    }

    public void initialize(BeanContainer beanContainer) {
        conversionProvider = beanContainer.get(ConversionProvider.class);
    }
}
