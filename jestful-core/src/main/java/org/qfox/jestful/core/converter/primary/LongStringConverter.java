package org.qfox.jestful.core.converter.primary;

import org.qfox.jestful.core.Parameter;
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
public class LongStringConverter implements StringConverter<Long> {

	public boolean support(Parameter parameter) {
		return parameter.getType() == long.class;
	}

	public String convert(Parameter parameter, Long source) {
		return String.valueOf(source);
	}

	public Long convert(Parameter parameter, String source) {
		return source != null && source.isEmpty() == false ? Long.valueOf(source) : 0;
	}

}
