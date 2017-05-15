package org.qfox.jestful.multiform.annotation;

import java.lang.annotation.*;

/**
 * Created by yangchangpei on 17/5/15.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Form {

    String extension();

    String mime();

}
