package org.qfox.jestful.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Description: 标识注解是一个参数注解的注解,参数标注了该注解的注解不能超过一个
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月12日 上午10:32:17
 *
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE })
public @interface Variable {

	/**
	 * 参数所在位置, 请求头/路径/查询字符串/请求体
	 * 
	 * @see {@link Position}
	 * @return 参数所在位置
	 */
	Position position();

	/**
	 * 
	 * <p>
	 * Description: 定义参数序列化后的所在位置
	 * </p>
	 * 
	 * <p>
	 * Company: 广州市俏狐信息科技有限公司
	 * </p>
	 * 
	 * @author Payne 646742615@qq.com
	 *
	 * @date 2016年4月12日 上午10:39:41
	 *
	 * @since 1.0.0
	 */
	public static enum Position {

		/**
		 * 请求头
		 */
		HEADER,
		/**
		 * 请求路径
		 */
		PATH,
		/**
		 * 查询字符串
		 */
		QUERY,
		/**
		 * 请求体
		 */
		BODY;

	}

}
