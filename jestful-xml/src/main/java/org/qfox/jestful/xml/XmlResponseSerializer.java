package org.qfox.jestful.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.formatting.ResponseSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;

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
 * @date 2016年4月9日 下午5:31:55
 * @since 1.0.0
 */
public class XmlResponseSerializer extends XmlMapper implements ResponseSerializer {
    private static final long serialVersionUID = 1842993874881568207L;
    private final String contentType = "application/xml";

    public String getContentType() {
        return contentType;
    }

    public void setSerializationDateFormat(DateFormat dateFormat) {
        this.setDateFormat(dateFormat);
    }

    public void serialize(Action action, MediaType mediaType, String charset, OutputStream out) throws IOException {
        OutputStreamWriter osw = null;
        try {
            action.getResponse().setContentType(contentType + ";charset=" + charset);
            osw = new OutputStreamWriter(out, charset);
            serialize(action, mediaType, osw);
        } finally {
            IOKit.close(osw);
        }
    }

    @Override
    public void serialize(Action action, MediaType mediaType, Writer writer) throws IOException {
        writeValue(writer, action.getResult().getBody().getValue());
    }

    @Override
    public void serialize(Object result, MediaType mediaType, String charset, OutputStream out) throws IOException {
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(out, charset);
            writeValue(osw, result);
        } finally {
            IOKit.close(osw);
        }
    }

}
