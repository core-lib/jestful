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
public class CharacterStringConverter implements StringConverter<Character> {

	public boolean support(Parameter parameter) {
		return parameter.getType() == Character.class;
	}

	public String convert(Parameter parameter, Character source) {
		return source == null ? null : String.valueOf(source);
	}

	public Character convert(Parameter parameter, String source) {
		if (source == null || source.isEmpty()) {
			return null;
		}
		if (source.length() != 1) {
			throw new IllegalArgumentException("can not converter string \"" + source + "\" to char");
		}
		return source.toCharArray()[0];
	}
}
