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
            String name = parameter.getName();
            String[] values = headerStringConversion.convert(parameter);
            if (values == null) {
                continue;
            }
            for (int i = 0; values != null && i < values.length; i++) {
                String value = values[i];
                String regex = parameter.getRegex();
                if (regex != null && value.matches(regex) == false) {
                    throw new IllegalArgumentException("converted value " + value + " does not matches regex " + regex);
                }
                if (parameter.isCoding() && !parameter.isEncoded()) {
                    values[i] = URLEncoder.encode(values[i], charset);
                }
            }
            request.setRequestHeaders(parameter.isCoding() && !parameter.isEncoded() ? URLEncoder.encode(name, charset) : name, values);
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
