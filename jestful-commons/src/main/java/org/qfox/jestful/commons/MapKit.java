package org.qfox.jestful.commons;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
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
            if (!map.containsKey(key)) {
                map.put(key, new String[0]);
            }
            String[] values = map.get(key);
            values = ArrayKit.copyOf(values, values.length + 1);
            values[values.length - 1] = value;
            map.put(key, values);
        }
        return map;
    }

    public static Map<String, List<String>> extract(String prefix, Map<String, List<String>> map) {
        if (prefix.isEmpty()) return Collections.unmodifiableMap(map);
        Map<String, List<String>> m = new LinkedHashMap<String, List<String>>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            if (key.equals(prefix)) m.put("", Collections.unmodifiableList(entry.getValue()));
            if (key.startsWith(prefix + ".")) m.put(entry.getKey().substring((prefix + ".").length()), Collections.unmodifiableList(entry.getValue()));
        }
        return Collections.unmodifiableMap(m);
    }

}
