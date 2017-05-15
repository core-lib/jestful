package org.qfox.jestful.multiview.annotation;

import java.lang.annotation.*;

/**
 * Created by yangchangpei on 17/5/15.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface View {

    String extension();

    String mime() default "";

    String path() default "";

    String attribute() default "";

}
