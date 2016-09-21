package org.qfox.jestful.core.converter.wrapper;

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
public class DoubleStringConverter implements StringConverter<Double> {

	public boolean support(Class<?> klass) {
		return klass == Double.class;
	}

	public String convert(Class<?> klass, Double source) {
		return source == null ? null : String.valueOf(source);
	}

	public Double convert(Class<?> klass, String source) {
		return source != null && source.length() == 0 == false ? Double.valueOf(source) : null;
	}

}
