package org.qfox.jestful.core.http;

import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.annotation.Variable;

import java.lang.annotation.*;

/**
 * <p>
 * Description: 请求路径参数注解
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月12日 上午10:27:45
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Variable(position = Position.MATRIX, coding = true)
public @interface Matrix {

    /**
     * 参数名称
     *
     * @return 矩阵变量名称
     */
    String value() default "";

    /**
     * 明确指定是哪个路径变量， 缺省情况下通过所有的路径变量里面取值。
     *
     * @return 路径变量名称
     */
    String path() default "";

    boolean encoded() default false;

    boolean decoded() default false;

}
