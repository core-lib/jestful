package org.qfox.jestful.server.resolver;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.server.converter.ConversionProvider;

import java.util.Map;

/**
 * Created by yangchangpei on 17/5/2.
 */
public class HeaderResolver implements Resolver {
    private ConversionProvider conversionProvider;

    @Override
    public boolean supports(Action action, Parameter parameter) {
        return parameter.getPosition() == Position.HEADER && parameter.getValue() == null;
    }

    @Override
    public void resolve(Action action, Parameter parameter) throws Exception {
        String charset = action.getHeaderEncodeCharset();
        Map<String, String[]> headers = action.getHeaders();

        boolean decoded = !parameter.isCoding() || (parameter.isCoding() && parameter.isDecoded());
        Object value = conversionProvider.convert(parameter.getName(), parameter.getType(), decoded, charset, headers);
        parameter.setValue(value);
    }

    public void initialize(BeanContainer beanContainer) {
        conversionProvider = beanContainer.get(ConversionProvider.class);
    }
}
