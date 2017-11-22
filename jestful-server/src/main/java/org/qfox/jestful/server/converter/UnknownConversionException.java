package org.qfox.jestful.server.converter;

import java.lang.reflect.Type;
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
 * @date 2016年4月22日 下午7:57:36
 * @since 1.0.0
 */
public class UnknownConversionException extends ConversionException {
    private static final long serialVersionUID = 6862946705238520404L;

    public UnknownConversionException(String message, String name, Type type, Map<String, String[]> map, ConversionProvider provider) {
        super(message, name, type, map, provider);
    }

    public UnknownConversionException(String message, Throwable cause, String name, Type type, Map<String, String[]> map, ConversionProvider provider) {
        super(message, cause, name, type, map, provider);
    }

    public UnknownConversionException(String name, Type type, Map<String, String[]> map, ConversionProvider provider) {
        super(name, type, map, provider);
    }

    public UnknownConversionException(Throwable cause, String name, Type type, Map<String, String[]> map, ConversionProvider provider) {
        super(cause, name, type, map, provider);
    }

}
