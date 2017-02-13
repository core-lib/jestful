package org.qfox.jestful.core.annotation;

import org.qfox.jestful.core.Position;

import java.lang.annotation.*;

/**
 * <p>
 * Description: 标识注解是一个参数注解的注解,参数标注了该注解的注解不能超过一个
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月12日 上午10:32:17
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface Variable {

    /**
     * 参数所在位置, 请求头/路径/查询字符串/请求体
     *
     * @return 参数所在位置
     * @see {@link Position}
     */
    Position position();

    /**
     * 参数是否可进行URL编码
     *
     * @return true: 如果可以URL编码, false: 否则
     */
    boolean coding();

}
