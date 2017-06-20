package org.qfox.jestful.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月9日 下午7:05:15
 * @since 1.0.0
 */
public class Disposition {
    private static final Pattern PATTERN = Pattern.compile("([^=]+?)=\"([^\"]*)\"");

    private final String type;
    private final String name;
    private final String filename;
    private final Map<String, String> parameters;

    private Disposition(String type, Map<String, String> parameters) {
        super();
        this.type = type;
        this.parameters = Collections.unmodifiableMap(parameters);
        this.name = parameters.get("name");
        this.filename = parameters.get("filename");
    }

    // form-data;name="picture";filename="u=3170428994,1730690014&fm=%22;ä¸­æ26&gp=0.jpg"
    public static Disposition valueOf(String disposition) {
        if (disposition == null) throw new NullPointerException();

        // 如果是空字符串
        if (disposition.trim().length() == 0) return new Disposition(null, new HashMap<String, String>());

        String[] parts = disposition.split(";");
        String type = parts[0];
        Map<String, String> parameters = new LinkedHashMap<String, String>();
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            // 如果格式不明确 则忽略
            if (!part.matches(PATTERN.pattern())) continue;
            Matcher matcher = PATTERN.matcher(part);
            if (!matcher.find()) continue;
            String name = matcher.group(1);
            String value = matcher.group(2);
            parameters.put(name, value);
        }

        return new Disposition(type, parameters);
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Disposition other = (Disposition) obj;
        if (parameters == null) {
            if (other.parameters != null)
                return false;
        } else if (!parameters.equals(other.parameters))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(type);
        for (Entry<String, String> entry : parameters.entrySet()) {
            builder.append("; ").append(entry.getKey()).append("=").append("\"").append(entry.getValue()).append("\"");
        }
        return builder.toString();
    }

}
