package org.qfox.jestful.cache;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangchangpei on 17/9/4.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cacheable {

    String value() default "";

    long duration() default 5L * 60L * 1000L;

    TimeUnit unit() default TimeUnit.MILLISECONDS;

    String condition() default "true";

    Class<? extends Generator> generator() default Generator.DEFAULT.class;

}
