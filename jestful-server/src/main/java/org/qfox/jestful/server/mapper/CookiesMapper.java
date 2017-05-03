package org.qfox.jestful.server.mapper;

import org.qfox.jestful.commons.ArrayKit;
import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Configurable;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.exception.BeanConfigException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yangchangpei on 17/5/3.
 */
public class CookiesMapper implements Actor, Configurable {
    private boolean caseInsensitive = true;

    @Override
    public Object react(Action action) throws Exception {
        Request request = action.getRequest();
        if (!(request instanceof HttpServletRequest)) return action.execute();

        String charset = action.getHeaderEncodeCharset();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Cookie[] cks = httpServletRequest.getCookies();
        Map<String, String[]> cookies = caseInsensitive ? new CaseInsensitiveMap<String, String[]>() : new LinkedHashMap<String, String[]>();
        for (int i = 0; cks != null && i < cks.length; i++) {
            Cookie cookie = cks[i];
            String key = URLDecoder.decode(cookie.getName(), charset);
            String value = cookie.getValue();
            if (!cookies.containsKey(key)) {
                cookies.put(key, new String[0]);
            }
            String[] values = cookies.get(key);
            values = ArrayKit.copyOf(values, values.length + 1);
            values[values.length - 1] = value;
            cookies.put(key, values);
        }

        action.setCookies(cookies);

        return action.execute();
    }

    @Override
    public void config(Map<String, String> arguments) throws BeanConfigException {
        this.caseInsensitive = arguments.containsKey("cookie-case-insensitive") ? Boolean.valueOf(arguments.get("cookie-case-insensitive")) : caseInsensitive;
    }
}
