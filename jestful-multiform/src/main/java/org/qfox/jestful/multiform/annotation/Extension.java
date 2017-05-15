package org.qfox.jestful.multiform.annotation;

import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.annotation.Variable;

import java.lang.annotation.*;

/**
 * Created by yangchangpei on 17/5/15.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
@Variable(position = Position.SESSION, coding = false)
public @interface Extension {
}
