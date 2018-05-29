package org.qfox.jestful.server.resolver;

import org.qfox.jestful.core.*;
import org.qfox.jestful.server.converter.ConversionProvider;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangchangpei on 17/5/2.
 */
public class PathResolver implements Resolver, Initialable {
    private ConversionProvider conversionProvider;

    @Override
    public boolean supports(Action action, Parameter parameter) {
        return parameter.getPosition() == Position.PATH && parameter.getValue() == null;
    }

    @Override
    public void resolve(Action action, Parameter parameter) throws Exception {
        String name = parameter.getName();
        Type type = parameter.getType();
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
        Map<String, String[]> map = Collections.singletonMap(name, new String[]{group});
        Object value = conversionProvider.convert(name, type, decoded, charset, map);
        parameter.setValue(value);
    }

    public void initialize(BeanContainer beanContainer) {
        this.conversionProvider = beanContainer.get(ConversionProvider.class);
    }
}
