package org.qfox.jestful.async.annotation;

import java.lang.annotation.*;

/**
 * Created by yangchangpei on 17/5/3.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Async {

    boolean value() default true;

    long timeout() default 0;

}
