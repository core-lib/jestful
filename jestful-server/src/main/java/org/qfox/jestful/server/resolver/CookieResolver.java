package org.qfox.jestful.server.resolver;

import org.qfox.jestful.commons.conversion.Conversion;
import org.qfox.jestful.commons.conversion.ConversionProvider;
import org.qfox.jestful.core.*;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by yangchangpei on 17/5/2.
 */
public class CookieResolver implements Resolver, Initialable {
    private ConversionProvider conversionProvider;

    @Override
    public boolean supports(Action action, Parameter parameter) {
        return parameter.between(Position.COOKIE) && parameter.getValue() == null;
    }

    @Override
    public void resolve(Action action, Parameter parameter) throws Exception {
        String charset = action.getHeaderEncodeCharset();
        Map<String, String[]> cookies = action.getCookies();

        String name = parameter.getName();
        Type type = parameter.getType();
        boolean decoded = !parameter.isCoding() || (parameter.isCoding() && parameter.isDecoded());
        for (Map.Entry<String, String[]> entry : cookies.entrySet()) {
            Conversion conversion = new Conversion(name, parameter.getValue(), type, decoded, charset, entry.getKey(), entry.getValue());
            Object value = conversionProvider.convert(conversion);
            parameter.setValue(value);
        }
    }

    @Override
    public void initialize(BeanContainer beanContainer) {
        this.conversionProvider = beanContainer.get(ConversionProvider.class);
    }

}
