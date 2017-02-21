package org.qfox.jestful.client.handler;

import org.qfox.jestful.client.annotation.Cookies;
import org.qfox.jestful.client.exception.IllegalCookieException;
import org.qfox.jestful.core.*;

import java.net.URLEncoder;

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
 * @date 2016年4月28日 下午9:41:34
 * @since 1.0.0
 */
public class CookiesAnnotationHandler implements Actor {

    public Object react(Action action) throws Exception {
        Request request = action.getRequest();
        String cookie = request.getRequestHeader("Cookie");
        cookie = cookie != null ? cookie : "";
        String charset = action.getHeaderEncodeCharset();
        Resource resource = action.getResource();
        if (resource.isAnnotationPresent(Cookies.class)) {
            Cookies cookies = resource.getAnnotation(Cookies.class);
            String[] values = cookies.value();
            for (String value : values) {
                String[] keyvalue = value.split("=");
                if (keyvalue.length != 2) {
                    throw new IllegalCookieException(value + " is not a key-value pair like key=value", value);
                }
                String k = keyvalue[0];
                String v = keyvalue[1];
                k = URLEncoder.encode(k, charset);
                if (cookies.encoded() == false) {
                    v = URLEncoder.encode(v, charset);
                }
                cookie += (cookie.length() == 0 ? "" : "; ") + k + "=" + v;
            }
        }

        Mapping mapping = action.getMapping();
        if (mapping.isAnnotationPresent(Cookies.class)) {
            Cookies cookies = mapping.getAnnotation(Cookies.class);
            String[] values = cookies.value();
            for (String value : values) {
                String[] keyvalue = value.split("=");
                if (keyvalue.length != 2) {
                    throw new IllegalCookieException(value + " is not a key-value pair like key=value", value);
                }
                String k = keyvalue[0];
                String v = keyvalue[1];
                if (cookies.encoded() == false) {
                    k = URLEncoder.encode(k, charset);
                    v = URLEncoder.encode(v, charset);
                }
                cookie += (cookie.length() == 0 ? "" : "; ") + k + "=" + v;
            }
        }

        request.setRequestHeader("Cookie", cookie.length() == 0 ? null : cookie);
        return action.execute();
    }

}
