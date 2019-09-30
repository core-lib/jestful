package org.qfox.jestful.core.http;

import org.qfox.jestful.core.annotation.Function;

import java.lang.annotation.*;

/**
 * <p>
 * Description: Mapping the annotated method to a specified or default path for http GET method
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年1月15日 下午7:56:39
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Function(name = "GET", acceptBody = false, returnBody = true, idempotent = true, protocol = HTTP.class, handler = "get-handler")
public @interface GET {

    /**
     * specify the path to map
     *
     * @return
     */
    String value() default "";

    /**
     * indicated some content types will return, by default, any content type can resolve the request
     *
     * @return
     */
    String[] produces() default {};

}
