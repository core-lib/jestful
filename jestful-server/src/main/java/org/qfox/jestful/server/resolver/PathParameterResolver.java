package org.qfox.jestful.server.resolver;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.converter.StringConversion;

import java.net.URLDecoder;
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
 * @date 2016年4月7日 下午8:36:13
 * @since 1.0.0
 */
public class PathParameterResolver implements Actor, Initialable {
    private StringConversion pathStringConversion;

    public Object react(Action action) throws Exception {
        String URI = action.getURI();
        String charset = action.getPathEncodeCharset();
        List<Parameter> parameters = action.getParameters().all(Position.PATH);
        Pattern pattern = action.getPattern();
        Matcher matcher = pattern.matcher(URI);
        matcher.find();
        for (Parameter parameter : parameters) {
            int group = parameter.getGroup();
            if (group <= 0) {
                continue;
            }
            String source = parameter.isCoding() && !parameter.isDecoded() ? URLDecoder.decode(matcher.group(group), charset) : matcher.group(group);
            pathStringConversion.convert(parameter, source);
        }
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
