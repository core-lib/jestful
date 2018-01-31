package org.qfox.jestful.core.http;

import org.qfox.jestful.core.annotation.Function;

import java.lang.annotation.*;

/**
 * <p>
 * Description: request server to get it's supported methods of specified URL or '*' to check the server's performance
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年6月1日 下午9:41:54
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Function(name = "OPTIONS", acceptBody = false, returnBody = false, idempotent = true, protocol = HTTP.class)
public @interface OPTIONS {

    /**
     * specify the path to map
     *
     * @return
     */
    String value() default "";

}
