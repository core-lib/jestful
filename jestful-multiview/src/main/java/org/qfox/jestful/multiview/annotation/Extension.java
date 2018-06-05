package org.qfox.jestful.multiview.annotation;

import org.qfox.jestful.core.annotation.Variable;

import java.lang.annotation.*;


/**
 * Created by yangchangpei on 17/5/15.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Variable(position = Extension.POSITION, coding = true)
public @interface Extension {

    int POSITION = 1 << 7;

    /**
     * 缺省值
     *
     * @return
     */
    String value() default "";

    boolean encoded() default false;

    boolean decoded() default false;

}
