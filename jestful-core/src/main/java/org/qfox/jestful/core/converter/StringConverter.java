package org.qfox.jestful.core.converter;

import org.qfox.jestful.core.Parameter;

/**
 * <p>
 * Description: 类型和字符串之间的转换
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月8日 上午9:54:12
 *
 * @since 1.0.0
 */
public interface StringConverter<T> {

	boolean support(Parameter parameter);

	String convert(Parameter parameter, T source);

	T convert(Parameter parameter, String source);

}
