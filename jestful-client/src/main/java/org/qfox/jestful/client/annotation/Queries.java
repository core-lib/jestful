package org.qfox.jestful.client.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * Description: 方法的固定query注解,用于定义每次调用标注的方法都带上{@link Queries#value()}定义的query数组
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月28日 下午4:08:13
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Queries {

    String[] value();

    boolean encoded() default false;

}
