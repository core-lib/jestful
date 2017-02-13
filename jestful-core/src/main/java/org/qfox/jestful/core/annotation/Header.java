package org.qfox.jestful.core.annotation;

import org.qfox.jestful.core.Position;

import java.lang.annotation.*;

/**
 * <p>
 * Description: 请求头参数注解
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月12日 上午10:15:24
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Variable(position = Position.HEADER, coding = true)
public @interface Header {

    /**
     * 参数名称
     *
     * @return
     */
    String value();

    boolean encoded() default false;

    boolean decoded() default false;

}
