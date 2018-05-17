package org.qfox.jestful.server.formatting;

import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.exception.JestfulIOException;
import org.qfox.jestful.server.converter.ConversionException;
import org.qfox.jestful.server.converter.ConversionProvider;
import org.qfox.jestful.server.converter.IncompatibleConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * 表单工具
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-17 15:39
 **/
public class FormKit {
    private static final Logger LOGGER = LoggerFactory.getLogger(FormKit.class);

    public static void assign(String charset, Map<String, String[]> map, List<Parameter> parameters, ConversionProvider conversionProvider) throws UnsupportedEncodingException, JestfulIOException {
        for (Parameter parameter : parameters) {
            if (parameter.getValue() != null) continue;
            try {
                boolean decoded = !parameter.isCoding() || (parameter.isCoding() && parameter.isDecoded());
                Object value = conversionProvider.convert(parameter.getName(), parameter.getType(), decoded, charset, map);
                parameter.setValue(value);
            } catch (IncompatibleConversionException e) {
                throw new JestfulIOException(e);
            } catch (ConversionException e) {
                LOGGER.warn("convert error", e);
            }
        }
    }

}
