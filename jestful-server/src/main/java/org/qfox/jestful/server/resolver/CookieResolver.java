package org.qfox.jestful.server.resolver;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.converter.StringConversion;

import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by yangchangpei on 17/5/2.
 */
public class CookieResolver implements Resolver, Initialable {
    private StringConversion stringConversion;

    @Override
    public boolean supports(Action action, Parameter parameter) {
        return parameter.getPosition() == Position.COOKIE && parameter.getValue() == null;
    }

    @Override
    public void resolve(Action action, Parameter parameter) throws Exception {
        String charset = action.getHeaderEncodeCharset();
        Map<String, String[]> cookies = action.getCookies();
        String name = parameter.getName();
        String[] values = cookies.get(name);
        if (values == null || values.length == 0 || values[0] == null) return;
        String value = values[0];
        boolean decoded = !parameter.isCoding() || (parameter.isCoding() && parameter.isDecoded());
        String source = decoded ? value : URLDecoder.decode(value, charset);
        stringConversion.convert(parameter, source);
    }

    @Override
    public void initialize(BeanContainer beanContainer) {
        this.stringConversion = beanContainer.get(StringConversion.class);
    }

}
