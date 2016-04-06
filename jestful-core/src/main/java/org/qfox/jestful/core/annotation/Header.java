package org.qfox.jestful.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Description: Specify the parameter value come from http headers by using {@link Header#value()} as the header name,
 * such as Content-Type/Transfer-Encoding or something else<br/>
 * There is an important fact you should know is the header to match is case ignored
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年1月15日 下午8:41:45
 *
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface Header {

	/**
	 * header name
	 * 
	 * @return
	 */
	String value();

}
