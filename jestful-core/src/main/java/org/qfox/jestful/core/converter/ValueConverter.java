package org.qfox.jestful.core.converter;

import java.lang.annotation.*;

/**
 * Created by yangchangpei on 17/7/1.
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValueConverter {
}
