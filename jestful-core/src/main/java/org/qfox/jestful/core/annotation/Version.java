package org.qfox.jestful.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Description: 方法或资源的版本注解, 如果方法上有该注解优先采用方法的, 如果方法上没有而controller有则采用controller的版本, 否则就没有版本, 基于RESTful的规范,
 * 通过Accept的请求头中的版本号信息区分请求的方法
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年5月13日 下午12:14:47
 *
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Version {

	String value();

}
