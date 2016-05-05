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
public class CharacterStringConverter implements StringConverter<Character> {

	public boolean support(Class<?> klass) {
		return klass == char.class;
	}

	public String convert(Class<?> klass, Character source) {
		return String.valueOf(source);
	}

	public Character convert(Class<?> klass, String source) {
		if (source == null || source.isEmpty()) {
			return 0;
		}
		if (source.length() != 1) {
			throw new IllegalArgumentException("can not converter string \"" + source + "\" to char");
		}
		return source.toCharArray()[0];
	}
}
