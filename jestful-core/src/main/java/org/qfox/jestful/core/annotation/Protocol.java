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
@Target({ElementType.ANNOTATION_TYPE})
public @interface Protocol {

    /**
     * 资源控制器访问协议
     *
     * @return 协议
     */
    String[] value();

}
