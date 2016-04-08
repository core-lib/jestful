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
 * @date 2016年4月8日 上午10:57:09
 *
 * @since 1.0.0
 */
public class DefaultStringConverter implements StringConverter<String> {

	public boolean support(Parameter parameter) {
		return parameter.getType() == String.class;
	}

	public String convert(Parameter parameter, String source) {
		return source;
	}

}
