package org.qfox.jestful.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.qfox.jestful.commons.Disposition;
import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.Multihead;
import org.qfox.jestful.commons.io.IOUtils;
import org.qfox.jestful.commons.io.MultipartOutputStream;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.formatting.RequestSerializer;

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
	private final String contentType = "application/xml";

	public String getContentType() {
		return contentType;
	}

	public boolean supports(Action action) {
		List<Parameter> bodies = action.getParameters().all(Position.BODY);
		return bodies.size() == 0 ? true : bodies.size() == 1 ? supports(bodies.get(0)) : false;
	}

	public boolean supports(Parameter parameter) {
		return parameter.getValue() != null ? canSerialize(parameter.getValue().getClass()) : true;
	}

	public void serialize(Action action, String charset, OutputStream out) throws IOException {
		List<Parameter> parameters = action.getParameters().all(Position.BODY);
		for (Parameter parameter : parameters) {
			OutputStreamWriter osw = null;
			try {
				action.getRequest().setRequestHeader("Content-Type", contentType + ";charset=" + charset);
				osw = new OutputStreamWriter(out, charset);
				writeValue(osw, parameter.getValue());
				break;
			} finally {
				IOUtils.close(osw);
			}
		}
	}

	public void serialize(Action action, Parameter parameter, String charset, MultipartOutputStream out) throws IOException {
		OutputStreamWriter osw = null;
		try {
			Disposition disposition = Disposition.valueOf("form-data; name=\"" + parameter.getName() + "\"");
			MediaType type = MediaType.valueOf(contentType + ";charset=" + charset);
			Multihead multihead = new Multihead(disposition, type);
			out.setNextMultihead(multihead);
			osw = new OutputStreamWriter(out, charset);
			writeValue(osw, parameter.getValue());
		} finally {
			IOUtils.close(osw);
		}
	}

}
