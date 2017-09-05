package org.qfox.jestful.cache.conversion;

import org.qfox.jestful.cache.Converter;

/**
 * Created by yangchangpei on 17/9/5.
 */
public interface KeyConverter extends Converter<Object> {

    boolean supports(Object source);

}
