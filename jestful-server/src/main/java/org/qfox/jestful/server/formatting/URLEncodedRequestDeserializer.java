package org.qfox.jestful.server.formatting;

import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.MapKit;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.JestfulIOException;
import org.qfox.jestful.core.formatting.RequestDeserializer;
import org.qfox.jestful.server.converter.ConversionException;
import org.qfox.jestful.server.converter.ConversionProvider;
import org.qfox.jestful.server.converter.IncompatibleConversionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
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
 * @date 2016年4月8日 下午5:23:18
 * @since 1.0.0
 */
public class URLEncodedRequestDeserializer implements RequestDeserializer, Initialable {
    private ConversionProvider conversionProvider;

    public String getContentType() {
        return "application/x-www-form-urlencoded";
    }

    public void deserialize(Action action, MediaType mediaType, String charset, InputStream in) throws IOException {
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            Map<String, String[]> map = new LinkedHashMap<String, String[]>();
            isr = new InputStreamReader(in);
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                map.putAll(MapKit.valueOf(line, charset));
            }
            List<Parameter> parameters = action.getParameters().all(Position.BODY);
            for (Parameter parameter : parameters) {
                if (parameter.getValue() != null) continue;
                try {
                    boolean decoded = parameter.isCoding() == false || (parameter.isCoding() && parameter.isDecoded());
                    Object value = conversionProvider.convert(parameter.getName(), parameter.getType(), decoded, charset, map);
                    parameter.setValue(value);
                } catch (IncompatibleConversionException e) {
                    throw new JestfulIOException(e);
                } catch (ConversionException e) {
                    continue;
                }
            }
        } finally {
            IOKit.close(br);
            IOKit.close(isr);
        }
    }

    public void deserialize(Action action, Parameter parameter, Multihead multihead, String charset, InputStream in) throws IOException {
        throw new UnsupportedOperationException();
    }

    public void initialize(BeanContainer beanContainer) {
        conversionProvider = beanContainer.get(ConversionProvider.class);
    }

}
