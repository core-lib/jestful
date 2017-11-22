package org.qfox.jestful.core.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 资源的操作方法的超时时间, 可以定义在资源或操作方法上, 定义在操作方法上的将拥有更高的优先权, 框架提供三种方式来定义超时时间<br/>
 * 1. 初始化配置 2. 资源控制器 3. 资源操作方法, 其中这三种超时时间的优先权依次递增
 *
 * @author Administrator
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Timeout {

    /**
     * 超时时间值
     *
     * @return
     */
    int value();

    /**
     * 超时时间单位, 默认为秒
     *
     * @return
     */
    TimeUnit unit() default TimeUnit.SECONDS;

}
