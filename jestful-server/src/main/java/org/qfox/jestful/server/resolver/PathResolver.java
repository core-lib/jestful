package org.qfox.jestful.server.resolver;

import org.qfox.jestful.commons.conversion.Conversion;
import org.qfox.jestful.commons.conversion.ConversionProvider;
import org.qfox.jestful.core.*;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangchangpei on 17/5/2.
 */
public class PathResolver implements Resolver, Initialable {
    private ConversionProvider conversionProvider;

    @Override
    public boolean supports(Action action, Parameter parameter) {
        return parameter.between(Position.PATH) && parameter.getValue() == null;
    }

    @Override
    public void resolve(Action action, Parameter parameter) throws Exception {
        String name = parameter.getName();
        Type type = parameter.getType();
        Object value = parameter.getValue();
        int index = parameter.getGroup();
        if (index <= 0) return;
        String URI = action.getServletURI();
        Pattern pattern = action.getPattern();
        String regex = pattern.pattern();
        if (!URI.endsWith("/") && regex.endsWith("/")) URI = URI.concat("/");
        String charset = action.getPathEncodeCharset();
        Matcher matcher = pattern.matcher(URI);
        if (!matcher.find()) throw new IllegalStateException("uri " + URI + " is not match with " + pattern);
        boolean decoded = !parameter.isCoding() || (parameter.isCoding() && parameter.isDecoded());
        String group = matcher.group(index);
        String[] values = new String[]{group};
        Conversion conversion = new Conversion(name, value, type, decoded, charset, name, values);
        value = conversionProvider.convert(conversion);
        parameter.setValue(value);
    }

    public void initialize(BeanContainer beanContainer) {
        this.conversionProvider = beanContainer.get(ConversionProvider.class);
    }
}
