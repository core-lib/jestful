package org.qfox.jestful.core.converter;

import java.util.Date;

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
 * @date 2016年5月5日 上午10:42:07
 *
 * @since 1.0.0
 */
public class DateStringConverter implements StringConverter<Date> {

	public boolean support(Class<?> klass) {
		return Date.class.isAssignableFrom(klass);
	}

	public String convert(Class<?> klass, Date source) {
		return String.valueOf(source.getTime());
	}

	public Date convert(Class<?> klass, String source) {
		try {
			return (Date) klass.getConstructor(long.class).newInstance(Long.valueOf(source));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
