package org.qfox.jestful.server.resolver;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.converter.StringConversion;

import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangchangpei on 17/5/2.
 */
public class PathResolver implements Resolver, Initialable {
    private StringConversion stringConversion;

    @Override
    public boolean supports(Action action, Parameter parameter) {
        return parameter.getPosition() == Position.PATH && parameter.getValue() == null;
    }

    @Override
    public void resolve(Action action, Parameter parameter) throws Exception {
        int group = parameter.getGroup();
        if (group <= 0) return;
        String URI = action.getURI();
        if (!URI.endsWith("/")) URI = URI.concat("/");
        String charset = action.getPathEncodeCharset();
        Pattern pattern = action.getPattern();
        Matcher matcher = pattern.matcher(URI);
        if (!matcher.find()) throw new IllegalStateException("uri " + URI + " is not match with " + pattern);
        String source = parameter.isCoding() && !parameter.isDecoded() ? URLDecoder.decode(matcher.group(group), charset) : matcher.group(group);
        stringConversion.convert(parameter, source);
    }

    public void initialize(BeanContainer beanContainer) {
        this.stringConversion = beanContainer.get(StringConversion.class);
    }
}
