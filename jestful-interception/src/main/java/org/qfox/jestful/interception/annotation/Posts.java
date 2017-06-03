package org.qfox.jestful.interception.annotation;

import org.qfox.jestful.core.annotation.POST;

import java.lang.annotation.*;

/**
 * Created by yangchangpei on 17/5/8.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Commands
public @interface Posts {

    POST[] value();

}