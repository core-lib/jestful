package org.qfox.jestful.interception;

import java.lang.annotation.*;

/**
 * Created by yangchangpei on 17/8/23.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Sequence {

    int value();

}
