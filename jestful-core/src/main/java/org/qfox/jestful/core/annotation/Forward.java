package org.qfox.jestful.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.qfox.jestful.core.Movement;

/**
 * <p>
 * Description: 该注解标注在方法上则表明该方法是一个跳转方法,通过{@link Forward#value()} 指定跳转的URI,可以采用JEXL表达式对URI的变量进行赋值.
 * {@link Forward#execute()} 代表是否执行当前这个方法, false: 直接跳转, true: 执行后再跳转
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月13日 上午10:32:12
 *
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Move(Movement.FORWARD)
public @interface Forward {

	/**
	 * 跳转URI,可以通过JEXL表达式进行赋值
	 * 
	 * @return
	 */
	String value();

	/**
	 * 是否执行当前方法后再跳转
	 * 
	 * @return true: 执行后跳转 false: 不执行直接跳转
	 */
	boolean execute() default true;

}
