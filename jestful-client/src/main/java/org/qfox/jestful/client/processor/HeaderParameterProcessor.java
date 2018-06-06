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
 * @date 2016年4月28日 下午7:59:19
 * @since 1.0.0
 */
public class HeaderParameterProcessor implements Actor, Initialable {
    private ConversionProvider headerConversionProvider;

    public Object react(Action action) throws Exception {
        Request request = action.getRequest();
        String charset = action.getHeaderEncodeCharset();
        List<Parameter> parameters = action.getParameters().all(Position.HEADER);
        for (Parameter parameter : parameters) {
            Map<String, String[]> map = headerConversionProvider.convert(parameter.getName(), parameter.getValue());
            if (map == null || map.isEmpty()) continue;
            for (Map.Entry<String, String[]> entry : map.entrySet()) {
                String name = entry.getKey();
                name = URLEncoder.encode(name, charset);
                String[] values = entry.getValue();
                String[] headers = new String[values.length];
                for (int i = 0; i < headers.length; i++) {
                    String value = values[i];
                    if (parameter.isCoding() && !parameter.isEncoded()) value = URLEncoder.encode(value, charset);
                    headers[i] = value;
                }
                if (headers.length > 0) request.setRequestHeaders(name, headers);
            }
        }
        return action.execute();
    }

    public void initialize(BeanContainer beanContainer) {
        this.headerConversionProvider = beanContainer.get(ConversionProvider.class);
    }

    public ConversionProvider getHeaderConversionProvider() {
        return headerConversionProvider;
    }

    public void setHeaderConversionProvider(ConversionProvider headerConversionProvider) {
        this.headerConversionProvider = headerConversionProvider;
    }

}
