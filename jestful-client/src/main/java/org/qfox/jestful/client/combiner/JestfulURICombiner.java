package org.qfox.jestful.client.combiner;

import org.qfox.jestful.commons.ArrayKit;
import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.commons.conversion.ConversionProvider;
import org.qfox.jestful.commons.conversion.ConvertingException;
import org.qfox.jestful.core.*;

import java.io.UnsupportedEncodingException;
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
            if (parameter.isCoding() && !parameter.isEncoded()) value = URLEncoder.encode(value, charset);
            String matrices = matrices(action, parameter);
            value = StringKit.isEmpty(matrices) ? value : value + ";" + matrices;

            Matcher matcher = pattern.matcher(resource.getExpression() + mapping.getExpression());
            int group = parameter.getGroup();
            for (int idx = 0; idx < group; idx++) if (!matcher.find()) throw new IllegalStateException("expected " + group + " path variable placeholders but actually got " + idx);
            String placeholder = matcher.group();
            uri = uri.replace(placeholder, value);
        }
        action.setRequestURI(uri.replaceAll("/+", "/"));
        return action.execute();
    }

    /**
     * 获取路径参数的矩阵参数
     *
     * @param action    请求动作
     * @param parameter 路径参数
     * @return 路径参数的矩阵参数
     * @throws ConvertingException          转换异常
     * @throws UnsupportedEncodingException 不支持的字符集异常
     */
    private String matrices(Action action, Parameter parameter) throws ConvertingException, UnsupportedEncodingException {
        String charset = action.getPathEncodeCharset();
        String name = parameter.getName();
        StringBuilder builder = new StringBuilder();
        int index = action.getParameters().all(Position.PATH).indexOf(parameter);
        List<Parameter> matrices = action.getParameters().all(Position.MATRIX);
        for (Parameter matrix : matrices) {
            // 如果矩阵变量的指定 path 域是该域 或者 矩阵变量没有指定某一个域而且该域是第一个域, 则放在该域
            String path = matrix.property("path");
            if (name.equals(path) || (StringKit.isEmpty(path) && index == 0)) {
                Map<String, String[]> map = pathConversionProvider.convert(matrix.getName(), matrix.getValue());
                for (Map.Entry<String, String[]> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String[] val = entry.getValue();
                    boolean encoding = matrix.isCoding() && !matrix.isEncoded();
                    if (encoding) for (int i = 0; i < val.length; i++) val[i] = URLEncoder.encode(val[i], charset);
                    if (builder.length() > 0) builder.append(";");
                    builder.append(URLEncoder.encode(key, charset));
                    builder.append("=");
                    builder.append(ArrayKit.concat(",", val));
                }
            }
        }
        return builder.toString();
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
