package org.qfox.jestful.cache.conversion;

import org.qfox.jestful.cache.Conversion;

import java.util.Date;

/**
 * Created by yangchangpei on 17/9/5.
 */
public class DateKeyConverter implements KeyConverter {

    @Override
    public boolean supports(Object source) {
        return source instanceof Date;
    }

    @Override
    public String convert(Object source, Conversion conversion) {
        return String.valueOf(((Date) source).getTime());
    }
}
