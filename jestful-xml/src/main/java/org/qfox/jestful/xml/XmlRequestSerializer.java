package org.qfox.jestful.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.Multihead;
import org.qfox.jestful.commons.io.IOUtils;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.RequestSerializer;

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
 * @date 2016年4月12日 下午6:50:18
 *
 * @since 1.0.0
 */
public class XmlRequestSerializer extends XmlMapper implements RequestSerializer {
	private static final long serialVersionUID = -240520430267871287L;

	public String getContentType() {
		return "application/xml";
	}

	public boolean supports(Parameter parameter) {
		return parameter.getValue() != null ? canSerialize(parameter.getValue().getClass()) : true;
	}

	public void serialize(Action action, MediaType mediaType, OutputStream out) throws IOException {
		List<Parameter> parameters = action.getParameters().all(Position.BODY);
		for (Parameter parameter : parameters) {
			OutputStreamWriter osw = null;
			try {
				osw = new OutputStreamWriter(out, mediaType.getCharset());
				writeValue(osw, parameter.getValue());
				break;
			} finally {
				IOUtils.close(osw);
			}
		}
	}

	public void serialize(Action action, Parameter parameter, Multihead multihead, OutputStream out) throws IOException {
		OutputStreamWriter osw = null;
		try {
			MediaType mediaType = multihead.getType();
			osw = new OutputStreamWriter(out, mediaType.getCharset());
			writeValue(osw, parameter.getValue());
		} finally {
			IOUtils.close(osw);
		}
	}

}
