package org.qfox.jestful.multiview;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.multiview.annotation.Extension;
import org.qfox.jestful.server.converter.ConversionProvider;
import org.qfox.jestful.server.resolver.Resolver;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

/**
 * Created by yangchangpei on 17/5/2.
 */
public class ExtensionResolver implements Resolver, Initialable {
    private ConversionProvider conversionProvider;

    @Override
    public boolean supports(Action action, Parameter parameter) {
        return parameter.getPosition() == Extension.POSITION && parameter.getValue() == null;
    }

    @Override
    public void resolve(Action action, Parameter parameter) throws Exception {
        String name = parameter.getName();
        Type type = parameter.getType();
        String URI = action.getRequestURI();
        String charset = action.getPathEncodeCharset();
        Extension extension = parameter.getAnnotation(Extension.class);
        String suffix = URI.contains(".") ? URI.substring(URI.lastIndexOf('.')) : extension.value();
        if (suffix.length() == 0) return;
        boolean decoded = !parameter.isCoding() || (parameter.isCoding() && parameter.isDecoded());
        Map<String, String[]> map = Collections.singletonMap(name, new String[]{suffix});
        conversionProvider.convert(name, type, decoded, charset, map);
    }

    public void initialize(BeanContainer beanContainer) {
        this.conversionProvider = beanContainer.get(ConversionProvider.class);
    }
}
