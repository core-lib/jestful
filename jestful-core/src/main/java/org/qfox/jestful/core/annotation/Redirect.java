package org.qfox.jestful.core.annotation;

/**
 * <p>
 * Description: 该注解标注在方法上则表明该方法是一个重定向方法,通过{@link Forward#value()} 指定重定向的URL,可以采用JEXL表达式对URL的变量进行赋值.
 * {@link Forward#execute()} 代表是否执行当前这个方法, false: 直接重定向, true: 执行后再重定向
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月13日 上午10:32:42
 *
 * @since 1.0.0
 */
public @interface Redirect {

	/**
	 * 重定向URL,可以通过JEXL表达式进行赋值
	 * 
	 * @return
	 */
	String value();

	/**
	 * 是否执行当前方法后再重定向
	 * 
	 * @return true: 执行后重定向 false: 不执行直接重定向
	 */
	boolean execute() default true;

}
