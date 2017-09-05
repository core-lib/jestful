package org.qfox.jestful.cache.conversion;

import org.qfox.jestful.cache.Conversion;

/**
 * Created by yangchangpei on 17/9/5.
 */
public class BooleanKeyConverter implements KeyConverter {

    @Override
    public boolean supports(Object source) {
        return source instanceof Boolean;
    }

    @Override
    public String convert(Object source, Conversion conversion) {
        return source.toString();
    }
}
