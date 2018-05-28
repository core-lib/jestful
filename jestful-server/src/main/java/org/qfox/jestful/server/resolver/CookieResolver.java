package org.qfox.jestful.server.resolver;

import org.qfox.jestful.core.*;
import org.qfox.jestful.server.converter.ConversionProvider;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

/**
 * Created by yangchangpei on 17/5/2.
 */
public class CookieResolver implements Resolver, Initialable {
    private ConversionProvider conversionProvider;

    @Override
    public boolean supports(Action action, Parameter parameter) {
        return parameter.getPosition() == Position.COOKIE && parameter.getValue() == null;
    }

    @Override
    public void resolve(Action action, Parameter parameter) throws Exception {
        String name = parameter.getName();
        Type type = parameter.getType();
        String charset = action.getHeaderEncodeCharset();
        Map<String, String[]> cookies = action.getCookies();
        String[] values = cookies.get(name);
        if (values == null || values.length == 0 || values[0] == null) return;
        boolean decoded = !parameter.isCoding() || (parameter.isCoding() && parameter.isDecoded());
        Map<String, String[]> map = Collections.singletonMap(name, values);
        Object value = conversionProvider.convert(name, type, decoded, charset, map);
        parameter.setValue(value);
    }

    @Override
    public void initialize(BeanContainer beanContainer) {
        this.conversionProvider = beanContainer.get(ConversionProvider.class);
    }

}
