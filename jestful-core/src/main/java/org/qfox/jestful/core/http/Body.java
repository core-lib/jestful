package org.qfox.jestful.core.http;

import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.annotation.Variable;

import java.lang.annotation.*;

/**
 * <p>
 * Description: 请求体参数注解
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月12日 上午10:28:06
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Variable(position = Position.BODY, coding = true)
public @interface Body {

    /**
     * 参数名称, 如果只有一个请求体则不需要给名称
     *
     * @return 参数名称
     */
    String value() default "*";

    boolean encoded() default false;

    boolean decoded() default false;

}
