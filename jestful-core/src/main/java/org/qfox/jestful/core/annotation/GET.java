package org.qfox.jestful.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Description: Mapping the annotated method to a specified or default path for http GET method
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年1月15日 下午7:56:39
 *
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Command(name = "GET", hasRequestBody = false, hasResponseBody = true, idempotent = true)
public @interface GET {

	/**
	 * specify the path to map
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * indicated some content types will return, by default, any content type can resolve the request
	 * 
	 * @return
	 */
	String[] produces() default {};

}
