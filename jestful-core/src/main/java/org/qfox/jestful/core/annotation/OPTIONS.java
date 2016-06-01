package org.qfox.jestful.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Description: request server to get it's supported methods of specified URL or '*' to check the server's performance
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年6月1日 下午9:41:54
 *
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Command(name = "OPTIONS", acceptBody = false, returnBody = false, idempotent = true)
public @interface OPTIONS {

	/**
	 * specify the path to map
	 * 
	 * @return
	 */
	String value() default "";

}
