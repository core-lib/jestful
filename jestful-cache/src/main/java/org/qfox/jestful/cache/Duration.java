package org.qfox.jestful.cache;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangchangpei on 17/9/5.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Duration {

    TimeUnit unit() default TimeUnit.MILLISECONDS;

}
