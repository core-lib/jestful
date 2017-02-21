package org.qfox.jestful.server.resolver;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.converter.StringConversion;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.List;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月20日 上午11:03:06
 * @since 1.0.0
 */
public class CookieParameterResolver implements Actor, Initialable {
    private StringConversion cookieStringConversion;
    private boolean caseInsensitive = true;

    public Object react(Action action) throws Exception {
        Request request = action.getRequest();
        if (request instanceof HttpServletRequest == false) {
            return action.execute();
        }
        String charset = action.getHeaderEncodeCharset();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        List<Parameter> parameters = action.getParameters().all(Position.COOKIE);
        for (Parameter parameter : parameters) {
            Cookie[] cookies = httpServletRequest.getCookies();
            for (int i = 0; cookies != null && i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                String cookieName = URLDecoder.decode(cookie.getName(), charset);
                String parameterName = parameter.getName();
                if (caseInsensitive ? cookieName.equalsIgnoreCase(parameterName) == false : cookieName.equals(parameterName) == false) {
                    continue;
                }
                String source = parameter.isCoding() && !parameter.isDecoded() ? URLDecoder.decode(cookie.getValue(), charset) : cookie.getValue();
                cookieStringConversion.convert(parameter, source);
            }
        }
        return action.execute();
    }

    public void initialize(BeanContainer beanContainer) {
        this.cookieStringConversion = beanContainer.get(StringConversion.class);
    }

    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    public void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }

    public StringConversion getCookieStringConversion() {
        return cookieStringConversion;
    }

    public void setCookieStringConversion(StringConversion cookieStringConversion) {
        this.cookieStringConversion = cookieStringConversion;
    }

}
