package org.qfox.jestful.core.converter.wrapper;

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
public class ShortStringConverter implements StringConverter<Short> {

	public boolean support(Parameter parameter) {
		return parameter.getType() == Short.class;
	}

	public String convert(Parameter parameter, Short source) {
		return source == null ? null : String.valueOf(source);
	}

	public Short convert(Parameter parameter, String source) {
		return source != null && source.isEmpty() == false ? Short.valueOf(source) : null;
	}

}
