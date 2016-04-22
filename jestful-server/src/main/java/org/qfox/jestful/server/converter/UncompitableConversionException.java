package org.qfox.jestful.server.converter;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月22日 下午6:48:19
 *
 * @since 1.0.0
 */
public class UncompitableConversionException extends ConversionException {
	private static final long serialVersionUID = -22337487046747971L;

	public UncompitableConversionException(String message, String name, Type type, Map<String, String[]> map, ConversionProvider provider) {
		super(message, name, type, map, provider);
	}

	public UncompitableConversionException(String message, Throwable cause, String name, Type type, Map<String, String[]> map, ConversionProvider provider) {
		super(message, cause, name, type, map, provider);
	}

	public UncompitableConversionException(String name, Type type, Map<String, String[]> map, ConversionProvider provider) {
		super(name, type, map, provider);
	}

	public UncompitableConversionException(Throwable cause, String name, Type type, Map<String, String[]> map, ConversionProvider provider) {
		super(cause, name, type, map, provider);
	}

}
