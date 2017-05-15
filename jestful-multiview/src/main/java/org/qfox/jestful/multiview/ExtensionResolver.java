package org.qfox.jestful.multiview;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.converter.StringConversion;
import org.qfox.jestful.multiview.annotation.Extension;
import org.qfox.jestful.server.resolver.Resolver;

import java.net.URLDecoder;

/**
 * Created by yangchangpei on 17/5/2.
 */
public class ExtensionResolver implements Resolver, Initialable {
    private StringConversion stringConversion;

    @Override
    public boolean supports(Action action, Parameter parameter) {
        return parameter.getPosition() == Extension.POSITION && parameter.getValue() == null;
    }

    @Override
    public void resolve(Action action, Parameter parameter) throws Exception {
        String URI = action.getURI();
        String charset = action.getPathEncodeCharset();
        Extension extension = parameter.getAnnotation(Extension.class);
        String suffix = URI.contains(".") ? URI.substring(URI.lastIndexOf('.') + 1) : extension.value();
        if (suffix.length() == 0) return;
        String source = parameter.isCoding() && !parameter.isDecoded() ? URLDecoder.decode(suffix, charset) : suffix;
        stringConversion.convert(parameter, source);
    }

    public void initialize(BeanContainer beanContainer) {
        this.stringConversion = beanContainer.get(StringConversion.class);
    }
}
