package org.qfox.jestful.cache;

import java.lang.annotation.*;

/**
 * Created by yangchangpei on 17/9/5.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Key {

    String value() default "";

    boolean ignored() default false;

    String condition() default "true";

    Class<? extends Converter<?>> converter() default Converter.DEFAULT.class;

    boolean flag() default false;

}
