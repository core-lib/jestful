package org.qfox.jestful.multiform.annotation;

import java.lang.annotation.*;

/**
 * Created by yangchangpei on 17/5/15.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Multiform {

    Form[] value() default {};

}
