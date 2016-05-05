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
public class FloatStringConverter implements StringConverter<Float> {

	public boolean support(Class<?> klass) {
		return klass == float.class;
	}

	public String convert(Class<?> klass, Float source) {
		return String.valueOf(source);
	}

	public Float convert(Class<?> klass, String source) {
		return source != null && source.isEmpty() == false ? Float.valueOf(source) : 0;
	}

}
