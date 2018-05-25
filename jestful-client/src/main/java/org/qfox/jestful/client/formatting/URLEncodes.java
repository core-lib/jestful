package org.qfox.jestful.client.formatting;

import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.converter.StringConversion;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * URL 编码工具类
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-25 14:39
 **/
public abstract class URLEncodes {

    public static String encode(String charset, List<Parameter> parameters, StringConversion stringConversion) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        for (Parameter parameter : parameters) {
            boolean encoding = parameter.isCoding() && !parameter.isEncoded();
            Map<String, List<String>> map = stringConversion.convert(parameter);
            if (map == null || map.isEmpty()) continue;
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                String name = entry.getKey();
                name = URLEncoder.encode(name, charset);
                List<String> values = entry.getValue();
                for (String value : values) {
                    if (encoding) value = URLEncoder.encode(value, charset);
                    builder.append(builder.length() == 0 ? "" : "&").append(name).append("=").append(value);
                }
            }
        }
        return builder.toString();
    }

}
