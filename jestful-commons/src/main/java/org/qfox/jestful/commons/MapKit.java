package org.qfox.jestful.commons;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
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

    public static Map<String, String[]> valueOf(String query, String charset) throws UnsupportedEncodingException {
        String[] pairs = query.split("&+");
        Map<String, String[]> map = new LinkedHashMap<String, String[]>();
        for (String pair : pairs) {
            String[] keyvalue = pair.split("=+");
            String key = URLDecoder.decode(keyvalue[0], charset);
            String value = keyvalue.length > 1 ? keyvalue[1] : "";
            if (map.containsKey(key) == false) {
                map.put(key, new String[0]);
            }
            String[] values = map.get(key);
            values = ArrayKit.copyOf(values, values.length + 1);
            values[values.length - 1] = value;
            map.put(key, values);
        }
        return map;
    }

}
