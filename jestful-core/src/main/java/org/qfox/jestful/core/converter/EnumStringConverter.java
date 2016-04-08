package org.qfox.jestful.core.converter;

import org.qfox.jestful.core.Parameter;

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
 * @date 2016年4月8日 上午10:58:17
 *
 * @since 1.0.0
 */
public class EnumStringConverter implements StringConverter<Enum<?>> {

	public boolean support(Parameter parameter) {
		if (parameter.getType() instanceof Class<?>) {
			Class<?> type = (Class<?>) parameter.getType();
			return Enum.class.isAssignableFrom(type);
		}
		return false;
	}

	public String convert(Parameter parameter, Enum<?> source) {
		return source.name();
	}

	public Enum<?> convert(Parameter parameter, String source) {
		try {
			Class<?> type = (Class<?>) parameter.getType();
			return (Enum<?>) type.getMethod("valueOf", String.class).invoke(null, source);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
