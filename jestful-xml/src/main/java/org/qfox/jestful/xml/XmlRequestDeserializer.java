package org.qfox.jestful.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.Multihead;
import org.qfox.jestful.commons.io.IOUtils;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.RequestDeserializer;

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
 * @date 2016年4月12日 下午5:48:00
 *
 * @since 1.0.0
 */
public class XmlRequestDeserializer extends XmlMapper implements RequestDeserializer {
	private static final long serialVersionUID = 5796735653228955284L;

	public String getContentType() {
		return "application/xml";
	}

	public void deserialize(Action action, MediaType mediaType, InputStream in) throws IOException {
		List<Parameter> parameters = action.getParameters().all(Position.BODY);
		for (Parameter parameter : parameters) {
			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(in, mediaType.getCharset());
				Type type = parameter.getType();
				Object value = readValue(isr, constructType(type));
				parameter.setValue(value);
				break;
			} finally {
				IOUtils.close(isr);
			}
		}
	}

	public void deserialize(Action action, Parameter parameter, Multihead multihead, InputStream in) throws IOException {
		InputStreamReader isr = null;
		try {
			MediaType mediaType = multihead.getType();
			isr = new InputStreamReader(in, mediaType.getCharset());
			Type type = parameter.getType();
			Object value = readValue(isr, constructType(type));
			parameter.setValue(value);
		} finally {
			IOUtils.close(isr);
		}
	}

}
