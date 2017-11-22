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
 * @date 2016年4月22日 下午5:42:36
 * @since 1.0.0
 */
public class UnsupportedConversionException extends ConversionException {
    private static final long serialVersionUID = 3708002562386491497L;

    public UnsupportedConversionException(String message, String name, Type type, Map<String, String[]> map, ConversionProvider provider) {
        super(message, name, type, map, provider);
    }

    public UnsupportedConversionException(String message, Throwable cause, String name, Type type, Map<String, String[]> map, ConversionProvider provider) {
        super(message, cause, name, type, map, provider);
    }

    public UnsupportedConversionException(String name, Type type, Map<String, String[]> map, ConversionProvider provider) {
        super(name, type, map, provider);
    }

    public UnsupportedConversionException(Throwable cause, String name, Type type, Map<String, String[]> map, ConversionProvider provider) {
        super(cause, name, type, map, provider);
    }

}
