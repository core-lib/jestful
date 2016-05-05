package org.qfox.jestful.core.converter.primary;

import org.qfox.jestful.core.converter.StringConverter;

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
 * @date 2016年4月8日 上午10:36:12
 *
 * @since 1.0.0
 */
public class ByteStringConverter implements StringConverter<Byte> {

	public boolean support(Class<?> klass) {
		return klass == byte.class;
	}

	public String convert(Class<?> klass, Byte source) {
		return String.valueOf(source);
	}

	public Byte convert(Class<?> klass, String source) {
		return source != null && source.isEmpty() == false ? Byte.valueOf(source) : 0;
	}

}
