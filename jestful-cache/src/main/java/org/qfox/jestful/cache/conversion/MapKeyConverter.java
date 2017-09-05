package org.qfox.jestful.cache.conversion;

import org.qfox.jestful.cache.Conversion;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by yangchangpei on 17/9/5.
 */
public class MapKeyConverter implements KeyConverter {

    @Override
    public boolean supports(Object source) {
        return source instanceof Map<?, ?>;
    }

    @Override
    public String convert(Object source, Conversion conversion) {
        Map<?, ?> map = (Map<?, ?>) source;
        Iterator<? extends Map.Entry<?, ?>> iterator = map.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        while (iterator.hasNext()) {
            Map.Entry<?, ?> entry = iterator.next();
            String key = conversion.convert(entry.getKey());
            String value = conversion.convert(entry.getValue());
            sb.append(key).append(":").append(value);
            if (iterator.hasNext()) sb.append(",");
        }
        sb.append("}");
        return sb.toString();
    }

}
