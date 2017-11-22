package org.qfox.jestful.client.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * Description: 方法的固定cookie注解,用于定义每次调用标注的方法都带上{@link Cookies#value()}定义的cookie数组
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月28日 下午4:08:32
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Cookies {

    String[] value();

    boolean encoded() default false;

}
