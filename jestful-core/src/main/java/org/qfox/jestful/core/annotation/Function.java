package org.qfox.jestful.core.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * Description: Make the annotation type to be a HTTP method by annotated this Annotation
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年1月15日 下午7:51:29
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface Function {

    /**
     * HTTP method name
     *
     * @return
     */
    String name();

    /**
     * does the HTTP method has request body?
     *
     * @return
     */
    boolean acceptBody();

    /**
     * does the HTTP method has response body?
     *
     * @return
     */
    boolean returnBody();

    /**
     * does the HTTP method is idempotent?
     *
     * @return
     */
    boolean idempotent();

    /**
     * transfer protocol
     *
     * @return transfer protocol
     */
    Class<? extends Annotation> protocol();

    /**
     * function handler name
     *
     * @return function handler name
     */
    String handler();

}
