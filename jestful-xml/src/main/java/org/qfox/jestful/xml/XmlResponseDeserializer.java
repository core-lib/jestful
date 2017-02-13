package org.qfox.jestful.xml;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Result;
import org.qfox.jestful.core.formatting.ResponseDeserializer;
import org.qfox.jestful.core.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

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
 * @date 2016年4月12日 下午7:21:30
 * @since 1.0.0
 */
public class XmlResponseDeserializer extends XmlMapper implements ResponseDeserializer {
    private static final long serialVersionUID = -7199443473873561462L;

    public XmlResponseDeserializer() {
        this.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        this.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        this.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        this.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        this.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
        this.configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, false);
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false);
    }

    public String getContentType() {
        return "application/xml";
    }

    public void deserialize(Action action, MediaType mediaType, String charset, InputStream in) throws IOException {
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(in, charset);
            deserialize(action, mediaType, isr);
        } finally {
            IOUtils.close(isr);
        }
    }

    @Override
    public void deserialize(Action action, MediaType mediaType, Reader reader) throws IOException {
        Result result = action.getResult();
        Object value = readValue(reader, constructType(result.getBody().getType()));
        result.getBody().setValue(value);
    }
}
