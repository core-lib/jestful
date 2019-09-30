package org.qfox.jestful.core.http;

import org.qfox.jestful.core.annotation.Function;

import java.lang.annotation.*;

/**
 * <p>
 * Description: Just get the resource' header from server like 'GET' method but does not got it's body
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年6月1日 下午9:41:04
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Function(name = "HEAD", acceptBody = false, returnBody = false, idempotent = true, protocol = HTTP.class, handler = "head-handler")
public @interface HEAD {

    /**
     * specify the path to map
     *
     * @return
     */
    String value() default "";

}
