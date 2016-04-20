package org.qfox.jestful.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.qfox.jestful.core.Position;

/**
 * <p>
 * Description: 请求cookie
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月20日 上午10:50:01
 *
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
@Variable(position = Position.COOKIE)
public @interface Cooky {

	/**
	 * cookie 名称, 不区分大小写!
	 * 
	 * @return
	 */
	String value();

}
