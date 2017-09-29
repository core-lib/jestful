package org.qfox.jestful.core;

import org.qfox.jestful.commons.collection.CaseInsensitiveMap;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
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
    private static final Pattern PATTERN = Pattern.compile(";\\s*([^=]+?)\\s*=\\s*\"([^\"]*)\"");

    private final String type;
    private final String name;
    private final String filename;
    private final Map<String, String> parameters;

    public Disposition(String type, String name) {
        this.type = type;
        this.name = name;
        this.filename = null;
        this.parameters = new CaseInsensitiveMap<String, String>();
        this.parameters.put("name", name);
    }

    public Disposition(String type, String name, String filename) {
        this.type = type;
        this.name = name;
        this.filename = filename;
        this.parameters = new CaseInsensitiveMap<String, String>();
        this.parameters.put("name", name);
        this.parameters.put("filename", filename);
    }

    private Disposition(String type, Map<String, String> parameters) {
        super();
        this.type = type;
        this.parameters = Collections.unmodifiableMap(parameters);
        this.name = parameters.get("name");
        this.filename = parameters.get("filename");
    }

    public static Disposition valueOf(String disposition) {
        try {
            return valueOf(disposition, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Disposition valueOf(String disposition, String charset) throws UnsupportedEncodingException {
        if (disposition == null) throw new NullPointerException();

        // 如果是空字符串
        if (disposition.trim().length() == 0) return new Disposition(null, new LinkedHashMap<String, String>());

        String type = URLDecoder.decode(disposition.split(";")[0].trim(), charset);
        Map<String, String> parameters = new LinkedHashMap<String, String>();
        Matcher matcher = PATTERN.matcher(disposition);
        while (matcher.find()) {
            String name = matcher.group(1);
            String value = matcher.group(2);
            parameters.put(URLDecoder.decode(name, charset), URLDecoder.decode(value, charset));
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
        try {
            return toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString(String charset) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder(URLEncoder.encode(type, charset));
        for (Entry<String, String> entry : parameters.entrySet()) {
            builder.append(";")
                    .append(URLEncoder.encode(entry.getKey(), charset))
                    .append("=")
                    .append("\"")
                    .append(URLEncoder.encode(entry.getValue(), charset))
                    .append("\"");
        }
        return builder.toString();
    }

}
