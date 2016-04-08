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
public class BooleanStringConverter implements StringConverter<Boolean> {

	public boolean support(Parameter parameter) {
		return parameter.getType() == boolean.class;
	}

	public String convert(Parameter parameter, Boolean source) {
		return String.valueOf(source);
	}

	public Boolean convert(Parameter parameter, String source) {
		return source != null && source.isEmpty() == false ? Boolean.valueOf(source) : false;
	}

}
