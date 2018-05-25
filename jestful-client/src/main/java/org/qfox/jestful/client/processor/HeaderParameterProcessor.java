package org.qfox.jestful.client.processor;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.converter.StringConversion;

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
 * @date 2016年4月28日 下午7:59:19
 * @since 1.0.0
 */
public class HeaderParameterProcessor implements Actor, Initialable {
    private StringConversion headerStringConversion;

    public Object react(Action action) throws Exception {
        Request request = action.getRequest();
        String charset = action.getHeaderEncodeCharset();
        List<Parameter> parameters = action.getParameters().all(Position.HEADER);
        for (Parameter parameter : parameters) {
            Map<String, List<String>> map = headerStringConversion.convert(parameter);
            if (map == null || map.isEmpty()) continue;
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                String name = entry.getKey();
                name = URLEncoder.encode(name, charset);
                List<String> values = entry.getValue();
                String[] headers = new String[values.size()];
                for (int i = 0; i < headers.length; i++) {
                    String value = values.get(i);
                    if (parameter.isCoding() && !parameter.isEncoded()) value = URLEncoder.encode(value, charset);
                    headers[i] = value;
                }
                if (headers.length > 0) request.setRequestHeaders(name, headers);
            }
        }
        return action.execute();
    }

    public void initialize(BeanContainer beanContainer) {
        this.headerStringConversion = beanContainer.get(StringConversion.class);
    }

    public StringConversion getHeaderStringConversion() {
        return headerStringConversion;
    }

    public void setHeaderStringConversion(StringConversion headerStringConversion) {
        this.headerStringConversion = headerStringConversion;
    }

}
