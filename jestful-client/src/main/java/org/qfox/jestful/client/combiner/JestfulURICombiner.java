package org.qfox.jestful.client.combiner;

import org.qfox.jestful.commons.conversion.ConversionProvider;
import org.qfox.jestful.core.*;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
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
    private final Pattern pattern = Pattern.compile("\\{[^{}]+?}");
    private ConversionProvider pathConversionProvider;

    public Object react(Action action) throws Exception {
        Resource resource = action.getResource();
        Mapping mapping = action.getMapping();
        String uri = resource.getExpression() + mapping.getExpression();
        String charset = action.getPathEncodeCharset();
        List<Parameter> parameters = action.getParameters().all(Position.PATH);
        for (Parameter parameter : parameters) {
            Map<String, String[]> map = pathConversionProvider.convert(parameter.getName(), parameter.getValue());
            String name = parameter.getName();
            String[] values = map.get(name);
            if (values == null || values.length == 0) throw new IllegalArgumentException("path variable " + name + " can not be null");
            String value = values[0];
            String regex = parameter.getRegex();
            if (regex != null && !value.matches(regex)) throw new IllegalArgumentException("converted value " + value + " does not matches regex " + regex);
            Matcher matcher = pattern.matcher(resource.getExpression() + mapping.getExpression());
            int group = parameter.getGroup();
            for (int i = 0; i < group; i++) if (!matcher.find()) throw new IllegalStateException("expected " + group + " path variable placeholders but actually got " + i);
            String placeholder = matcher.group();
            if (parameter.isCoding() && !parameter.isEncoded()) value = URLEncoder.encode(value, charset);
            uri = uri.replace(placeholder, value);
        }
        action.setRequestURI(uri.replaceAll("/+", "/"));
        return action.execute();
    }

    public void initialize(BeanContainer beanContainer) {
        this.pathConversionProvider = beanContainer.get(ConversionProvider.class);
    }

    public ConversionProvider getPathConversionProvider() {
        return pathConversionProvider;
    }

    public void setPathConversionProvider(ConversionProvider pathConversionProvider) {
        this.pathConversionProvider = pathConversionProvider;
    }

}
