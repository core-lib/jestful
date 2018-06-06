package org.qfox.jestful.client.processor;

import org.qfox.jestful.commons.conversion.ConversionProvider;
import org.qfox.jestful.core.*;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

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
    private ConversionProvider cookieConversionProvider;

    public Object react(Action action) throws Exception {
        Request request = action.getRequest();
        String cookie = request.getRequestHeader("Cookie");
        cookie = cookie != null ? cookie : "";
        String charset = action.getHeaderEncodeCharset();
        StringBuilder builder = new StringBuilder(cookie);
        List<Parameter> parameters = action.getParameters().all(Position.COOKIE);
        for (Parameter parameter : parameters) {
            Map<String, String[]> map = cookieConversionProvider.convert(parameter.getName(), parameter.getValue());
            if (map == null || map.isEmpty()) continue;
            for (Map.Entry<String, String[]> entry : map.entrySet()) {
                String name = entry.getKey();
                name = URLEncoder.encode(name, charset);
                String[] values = entry.getValue();
                for (String value : values) {
                    if (parameter.isCoding() && !parameter.isEncoded()) value = URLEncoder.encode(value, charset);
                    builder.append(builder.length() == 0 ? "" : "; ").append(name).append("=").append(value);
                }
            }
        }
        if (builder.length() > 0) request.setRequestHeader("Cookie", builder.toString());
        return action.execute();
    }

    public void initialize(BeanContainer beanContainer) {
        this.cookieConversionProvider = beanContainer.get(ConversionProvider.class);
    }

    public ConversionProvider getCookieConversionProvider() {
        return cookieConversionProvider;
    }

    public void setCookieConversionProvider(ConversionProvider cookieConversionProvider) {
        this.cookieConversionProvider = cookieConversionProvider;
    }

}
