package org.qfox.jestful.cache;

import java.lang.annotation.*;

/**
 * Created by yangchangpei on 17/9/5.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Refresh {
}
