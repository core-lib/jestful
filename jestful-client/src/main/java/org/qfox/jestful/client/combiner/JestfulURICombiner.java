package org.qfox.jestful.client.combiner;

import org.qfox.jestful.commons.ArrayKit;
import org.qfox.jestful.commons.StringKit;
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
        for (int index = 0; index < parameters.size(); index++) {
            Parameter parameter = parameters.get(index);
            Map<String, String[]> map = pathConversionProvider.convert(parameter.getName(), parameter.getValue());
            String name = parameter.getName();
            String[] values = map.get(name);
            if (values == null || values.length == 0) throw new IllegalArgumentException("path variable " + name + " can not be null");
            String value = values[0];
            String regex = parameter.getRegex();
            if (regex != null && !value.matches(regex)) throw new IllegalArgumentException("converted value " + value + " does not matches regex " + regex);

            // 处理矩阵变量
            StringBuilder mixed = new StringBuilder(value);
            List<Parameter> matrices = action.getParameters().all(Position.MATRIX);
            for (Parameter matrix : matrices) {
                // 如果矩阵变量的指定 path 域是该域 或者 矩阵变量没有指定某一个域而且该域是第一个域, 则放在该域
                String path = matrix.property("path");
                if (name.equals(path) || (StringKit.isEmpty(path) && index == 0)) {
                    Map<String, String[]> m = pathConversionProvider.convert(matrix.getName(), matrix.getValue());
                    for (Map.Entry<String, String[]> entry : m.entrySet()) {
                        String key = entry.getKey();
                        String[] val = entry.getValue();
                        boolean encoding = matrix.isCoding() && !matrix.isEncoded();
                        if (encoding) for (int i = 0; i < val.length; i++) val[i] = URLEncoder.encode(val[i], charset);
                        mixed.append(";");
                        mixed.append(URLEncoder.encode(key, charset));
                        mixed.append("=");
                        mixed.append(ArrayKit.concat(",", val));
                    }
                }
            }
            value = mixed.toString();

            Matcher matcher = pattern.matcher(resource.getExpression() + mapping.getExpression());
            int group = parameter.getGroup();
            for (int idx = 0; idx < group; idx++) if (!matcher.find()) throw new IllegalStateException("expected " + group + " path variable placeholders but actually got " + idx);
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
