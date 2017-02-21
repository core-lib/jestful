package org.qfox.jestful.server.resolver;

import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.JestfulIOException;
import org.qfox.jestful.server.converter.ConversionException;
import org.qfox.jestful.server.converter.ConversionProvider;
import org.qfox.jestful.server.converter.IncompatibleConversionException;

import java.net.URLDecoder;
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
 * @date 2016年4月8日 下午12:08:44
 * @since 1.0.0
 */
public class HeaderParameterResolver implements Actor, Initialable {
    private ConversionProvider headerConversionProvider;

    public Object react(Action action) throws Exception {
        Map<String, String[]> map = new CaseInsensitiveMap<String, String[]>();
        Request request = action.getRequest();
        String charset = action.getHeaderEncodeCharset();
        String[] keys = request.getHeaderKeys();
        for (String key : keys) {
            String[] values = request.getRequestHeaders(key);
            map.put(URLDecoder.decode(key, charset), values);
        }
        List<Parameter> parameters = action.getParameters().all(Position.HEADER);
        for (Parameter parameter : parameters) {
            if (parameter.getValue() != null) {
                continue;
            }
            try {
                boolean decoded = parameter.isCoding() == false || (parameter.isCoding() && parameter.isDecoded());
                Object value = headerConversionProvider.convert(parameter.getName(), parameter.getType(), decoded, charset, map);
                parameter.setValue(value);
            } catch (IncompatibleConversionException e) {
                throw new JestfulIOException(e);
            } catch (ConversionException e) {
                continue;
            }
        }
        return action.execute();
    }

    public void initialize(BeanContainer beanContainer) {
        headerConversionProvider = beanContainer.get(ConversionProvider.class);
    }

    public ConversionProvider getHeaderConversionProvider() {
        return headerConversionProvider;
    }

    public void setHeaderConversionProvider(ConversionProvider headerConversionProvider) {
        this.headerConversionProvider = headerConversionProvider;
    }

}
