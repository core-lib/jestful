package org.qfox.jestful.commons;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Payne on 2017/4/9.
 */
public class MapKit {

    public static Map<String, String> valueOf(String text) {
        if (text == null) return null;
        String[] sections = text.split(",\\s*");
        Map<String, String> map = new LinkedHashMap<String, String>(sections.length);
        for (String section : sections) {
            int index = section.indexOf(':');
            if (index < 0) continue;
            String key = section.substring(0, index).trim();
            String value = section.substring(index + 1).trim();
            map.put(key, value);
        }
        return map;
    }

}
