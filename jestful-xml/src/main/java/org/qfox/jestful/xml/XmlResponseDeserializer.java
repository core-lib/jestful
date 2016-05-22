package org.qfox.jestful.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Result;
import org.qfox.jestful.core.formatting.ResponseDeserializer;
import org.qfox.jestful.core.io.IOUtils;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月12日 下午7:21:30
 *
 * @since 1.0.0
 */
public class XmlResponseDeserializer extends XmlMapper implements ResponseDeserializer {
	private static final long serialVersionUID = -7199443473873561462L;

	public String getContentType() {
		return "application/xml";
	}

	public void deserialize(Action action, MediaType mediaType, String charset, InputStream in) throws IOException {
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(in, charset);
			Result result = action.getResult();
			Object value = readValue(isr, constructType(result.getBody().getType()));
			result.getBody().setValue(value);
		} finally {
			IOUtils.close(isr);
		}
	}

}
