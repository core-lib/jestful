package org.qfox.jestful.client.processor;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.converter.StringConversion;

import java.net.URLEncoder;
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
 * @date 2016年4月28日 下午8:12:17
 * @since 1.0.0
 */
public class CookieParameterProcessor implements Actor, Initialable {
    private StringConversion cookieStringConversion;

    public Object react(Action action) throws Exception {
        Request request = action.getRequest();
        String cookie = request.getRequestHeader("Cookie");
        cookie = cookie != null ? cookie : "";
        String charset = action.getHeaderEncodeCharset();
        List<Parameter> parameters = action.getParameters().all(Position.COOKIE);
        for (Parameter parameter : parameters) {
            String[] values = cookieStringConversion.convert(parameter);
            for (int i = 0; values != null && i < values.length; i++) {
                String value = values[i];
                String regex = parameter.getRegex();
                if (regex != null && value.matches(regex) == false) {
                    throw new IllegalArgumentException("converted value " + value + " does not matches regex " + regex);
                }
                String name = parameter.getName();
                name = URLEncoder.encode(name, charset);
                if (parameter.isCoding() && !parameter.isEncoded()) {
                    value = URLEncoder.encode(value, charset);
                }
                cookie += (cookie.length() == 0 ? "" : "; ") + name + "=" + value;
            }
        }
        request.setRequestHeader("Cookie", cookie.length() == 0 ? null : cookie);
        return action.execute();
    }

    public void initialize(BeanContainer beanContainer) {
        this.cookieStringConversion = beanContainer.get(StringConversion.class);
    }

    public StringConversion getCookieStringConversion() {
        return cookieStringConversion;
    }

    public void setCookieStringConversion(StringConversion cookieStringConversion) {
        this.cookieStringConversion = cookieStringConversion;
    }

}
