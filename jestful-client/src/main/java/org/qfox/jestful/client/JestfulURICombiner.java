package org.qfox.jestful.client;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.converter.StringConversion;

import java.net.URLEncoder;
import java.util.List;
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
 * @date 2016年4月28日 下午2:47:59
 * @since 1.0.0
 */
public class JestfulURICombiner implements Actor, Initialable {
    private final Pattern pattern = Pattern.compile("\\{[^{}]+?\\}");
    private StringConversion pathStringConversion;

    public Object react(Action action) throws Exception {
        Resource resource = action.getResource();
        Mapping mapping = action.getMapping();
        String URI = resource.getExpression() + mapping.getExpression();
        String charset = action.getPathEncodeCharset();
        List<Parameter> parameters = action.getParameters().all(Position.PATH);
        for (Parameter parameter : parameters) {
            String[] values = pathStringConversion.convert(parameter);
            String value = values != null && values.length > 0 && values[0] != null ? values[0] : "null";
            String regex = parameter.getRegex();
            if (regex != null && !value.matches(regex)) {
                throw new IllegalArgumentException("converted value " + value + " does not matches regex " + regex);
            }
            Matcher matcher = pattern.matcher(resource.getExpression() + mapping.getExpression());
            int group = parameter.getGroup();
            for (int i = 0; i < group; i++) {
                matcher.find();
            }
            String variable = matcher.group();
            if (parameter.isCoding() && !parameter.isEncoded()) {
                value = URLEncoder.encode(value, charset);
            }
            URI = URI.replace(variable, value);
        }
        action.setURI(URI.replaceAll("/+", "/"));
        return action.execute();
    }

    public void initialize(BeanContainer beanContainer) {
        this.pathStringConversion = beanContainer.get(StringConversion.class);
    }

    public StringConversion getPathStringConversion() {
        return pathStringConversion;
    }

    public void setPathStringConversion(StringConversion pathStringConversion) {
        this.pathStringConversion = pathStringConversion;
    }

}
