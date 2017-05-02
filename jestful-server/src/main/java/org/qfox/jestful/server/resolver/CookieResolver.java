package org.qfox.jestful.server.resolver;

import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.converter.StringConversion;
import org.qfox.jestful.core.exception.BeanConfigException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yangchangpei on 17/5/2.
 */
public class CookieResolver implements Resolver, Initialable, Configurable {
    private StringConversion stringConversion;
    private boolean caseInsensitive = true;

    @Override
    public boolean supports(Action action, Parameter parameter) {
        return false;
    }

    @Override
    public void resolve(Action action, Parameter parameter) throws Exception {
        Request request = action.getRequest();
        if (!(request instanceof HttpServletRequest)) {
            return;
        }
        String charset = action.getHeaderEncodeCharset();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Cookie[] cookies = httpServletRequest.getCookies();
        Map<String, String> map = caseInsensitive ? new CaseInsensitiveMap<String, String>() : new LinkedHashMap<String, String>();
        for (int i = 0; cookies != null && i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            map.put(URLDecoder.decode(cookie.getName(), charset), cookie.getValue());
        }
        String name = parameter.getName();
        String value = map.get(name);
        boolean decoded = !parameter.isCoding() || (parameter.isCoding() && parameter.isDecoded());
        String source = decoded ? value : URLDecoder.decode(value, charset);
        stringConversion.convert(parameter, source);
    }

    @Override
    public void initialize(BeanContainer beanContainer) {
        this.stringConversion = beanContainer.get(StringConversion.class);
    }

    @Override
    public void config(Map<String, String> arguments) throws BeanConfigException {
        this.caseInsensitive = arguments.containsKey("cookie-case-insensitive") ? Boolean.valueOf(arguments.get("cookie-case-insensitive")) : caseInsensitive;
    }
}
