package org.qfox.jestful.json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.io.IOUtils;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.formatting.ResponseSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

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
 * @date 2016年4月9日 下午5:31:55
 *
 * @since 1.0.0
 */
public class JsonResponseSerializer extends ObjectMapper implements ResponseSerializer {
	private static final long serialVersionUID = 1842993874881568207L;

	public String getContentType() {
		return "application/json";
	}

	public void serialize(Action action, MediaType mediaType, String charset, OutputStream out) throws IOException {
		OutputStreamWriter osw = null;
		try {
			osw = new OutputStreamWriter(out, charset);
			writeValue(osw, action.getResult().getValue());
		} finally {
			IOUtils.close(osw);
		}
	}

}
