package org.qfox.jestful.cache.conversion;

import org.qfox.jestful.cache.Conversion;

import java.lang.reflect.Array;

/**
 * Created by yangchangpei on 17/9/5.
 */
public class ArrayKeyConverter implements KeyConverter {

    @Override
    public boolean supports(Object source) {
        return source.getClass().isArray();
    }

    @Override
    public String convert(Object source, Conversion conversion) {
        int len = Array.getLength(source);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < len; i++) {
            Object element = Array.get(source, i);
            String key = conversion.convert(element);
            sb.append(key);
            if (i < len - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
