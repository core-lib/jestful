package org.qfox.jestful.server.resolver;

import org.qfox.jestful.commons.conversion.ConversionProvider;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatrixResolver implements Resolver {
    private ConversionProvider conversionProvider;

    @Override
    public boolean supports(Action action, Parameter parameter) {
        return parameter.between(Position.MATRIX) && parameter.getValue() == null;
    }

    @Override
    public void resolve(Action action, Parameter parameter) throws Exception {
        String URI = action.getServletURI();
        Pattern pattern = action.getPattern();
        String regex = pattern.pattern();
        if (!URI.endsWith("/") && regex.endsWith("/")) URI = URI.concat("/");
        String charset = action.getPathEncodeCharset();
        Matcher matcher = pattern.matcher(URI);
        if (!matcher.find()) throw new IllegalStateException("uri " + URI + " is not match with " + pattern);
        boolean decoded = !parameter.isCoding() || (parameter.isCoding() && parameter.isDecoded());
        String path = parameter.property("path");
        int count = matcher.groupCount();
        for (int index = 1; index <= count; index++) {
            String group = matcher.group(index);

        }
    }

    public void initialize(BeanContainer beanContainer) {
        this.conversionProvider = beanContainer.get(ConversionProvider.class);
    }
}
