package org.qfox.jestful.server.annotation;

import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.annotation.Variable;

import java.lang.annotation.*;

/**
 * <p>
 * Description: 查询字符串参数注解
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月12日 上午10:28:00
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Variable(position = Field.POSITION, coding = true)
public @interface Field {

    int POSITION = Position.QUERY | Position.BODY;

    String value() default "";

    boolean encoded() default false;

    boolean decoded() default false;

}
