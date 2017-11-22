package org.qfox.jestful.core.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * Description: Make a class to be an resource controller class for RESTful API.
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年1月15日 下午8:15:41
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Jestful {

    /**
     * resource name
     *
     * @return
     */
    String value();

}
