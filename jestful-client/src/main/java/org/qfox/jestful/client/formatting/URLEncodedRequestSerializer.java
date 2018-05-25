package org.qfox.jestful.client.formatting;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.converter.StringConversion;
import org.qfox.jestful.core.formatting.RequestSerializer;
import org.qfox.jestful.core.io.MultipartOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

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
 * @date 2016年5月4日 下午4:26:25
 * @since 1.0.0
 */
public class URLEncodedRequestSerializer implements RequestSerializer, Initialable {
    private final String contentType = "application/x-www-form-urlencoded";
    private StringConversion urlStringConversion;

    public String getContentType() {
        return contentType;
    }

    public boolean supports(Action action) {
        List<Parameter> bodies = action.getParameters().all(Position.BODY);
        for (Parameter body : bodies) if (!supports(body)) return false;
        return true;
    }

    public boolean supports(Parameter parameter) {
        return urlStringConversion.supports(parameter);
    }

    public void serialize(Action action, String charset, OutputStream out) throws IOException {
        List<Parameter> parameters = action.getParameters().all(Position.BODY);
        String encode = URLEncodes.encode(charset, parameters, urlStringConversion);
        if (!encode.isEmpty()) {
            action.getRequest().setContentType(contentType);
            out.write(encode.getBytes());
        }
    }

    public void serialize(Action action, Parameter parameter, String charset, MultipartOutputStream out) throws IOException {
        if (parameter.getValue() == null) return;
        String name = parameter.getName();
        Map<String, List<String>> map = urlStringConversion.convert(parameter);
        if (map == null || map.isEmpty()) return;
        List<String> values = map.get(name);
        for (String value : values) {
            Disposition disposition = new Disposition("form-data", name);
            Multihead multihead = new Multihead(disposition, MediaType.valueOf(contentType + "; charset=" + charset));
            out.setNextMultihead(multihead);
            out.write(URLEncoder.encode(value, charset).getBytes());
        }
    }

    public void initialize(BeanContainer beanContainer) {
        this.urlStringConversion = beanContainer.get(StringConversion.class);
    }

    public StringConversion getUrlStringConversion() {
        return urlStringConversion;
    }

    public void setUrlStringConversion(StringConversion urlStringConversion) {
        this.urlStringConversion = urlStringConversion;
    }

}
