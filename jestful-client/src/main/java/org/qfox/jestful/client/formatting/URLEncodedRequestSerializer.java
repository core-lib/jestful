package org.qfox.jestful.client.formatting;

import org.qfox.jestful.commons.conversion.ConversionProvider;
import org.qfox.jestful.commons.conversion.ConvertingException;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.formatting.RequestSerializer;
import org.qfox.jestful.core.io.MultipartOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
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
    private ConversionProvider urlConversionProvider;

    public String getContentType() {
        return contentType;
    }

    public void setSerializationDateFormat(DateFormat dateFormat) {
        urlConversionProvider.setSerializationDateFormat(dateFormat);
    }

    public boolean supports(Action action) {
        List<Parameter> bodies = action.getParameters().all(Position.BODY);
        for (Parameter body : bodies) if (!supports(body)) return false;
        return true;
    }

    public boolean supports(Parameter parameter) {
        return urlConversionProvider.supports(parameter.getKlass());
    }

    public void serialize(Action action, String charset, OutputStream out) throws IOException {
        try {
            List<Parameter> parameters = action.getParameters().all(Position.BODY);
            String encode = URLEncodes.encode(charset, parameters, urlConversionProvider);
            if (!encode.isEmpty()) {
                action.getRequest().setContentType(contentType);
                out.write(encode.getBytes());
            }
        } catch (ConvertingException e) {
            throw new IOException(e);
        }
    }

    public void serialize(Action action, Parameter parameter, String charset, MultipartOutputStream out) throws IOException {
        try {
            if (parameter.getValue() == null) return;
            String name = parameter.getName();
            Map<String, String[]> map = urlConversionProvider.convert(parameter.getName(), parameter.getValue());
            if (map == null || map.isEmpty()) return;
            String[] values = map.get(name);
            for (String value : values) {
                Disposition disposition = new Disposition("form-data", name);
                Multihead multihead = new Multihead(disposition, MediaType.valueOf(contentType + "; charset=" + charset));
                out.setNextMultihead(multihead);
                out.write(URLEncoder.encode(value, charset).getBytes());
            }
        } catch (ConvertingException e) {
            throw new IOException(e);
        }
    }

    public void initialize(BeanContainer beanContainer) {
        this.urlConversionProvider = beanContainer.get(ConversionProvider.class);
    }

    public ConversionProvider getUrlConversionProvider() {
        return urlConversionProvider;
    }

    public void setUrlConversionProvider(ConversionProvider urlConversionProvider) {
        this.urlConversionProvider = urlConversionProvider;
    }

}
