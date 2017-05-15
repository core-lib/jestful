package org.qfox.jestful.multiview.annotation;

import java.lang.annotation.*;

/**
 * Created by yangchangpei on 17/5/15.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Views {

    View[] value() default {};

}
