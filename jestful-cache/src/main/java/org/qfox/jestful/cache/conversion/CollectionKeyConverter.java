package org.qfox.jestful.cache.conversion;

import org.qfox.jestful.cache.Conversion;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by yangchangpei on 17/9/5.
 */
public class CollectionKeyConverter implements KeyConverter {

    @Override
    public boolean supports(Object source) {
        return source instanceof Collection<?>;
    }

    @Override
    public String convert(Object source, Conversion conversion) {
        Collection<?> collection = (Collection<?>) source;
        Iterator<?> iterator = collection.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        while (iterator.hasNext()) {
            Object element = iterator.next();
            String key = conversion.convert(element);
            sb.append(key);
            if (iterator.hasNext()) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
