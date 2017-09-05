package org.qfox.jestful.cache.conversion;

import org.qfox.jestful.cache.Conversion;

/**
 * Created by yangchangpei on 17/9/5.
 */
public class StringKeyConverter implements KeyConverter {

    @Override
    public boolean supports(Object source) {
        return source instanceof CharSequence;
    }

    @Override
    public String convert(Object source, Conversion conversion) {
        return "\"" + source.toString() + "\"";
    }
}
