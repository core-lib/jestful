package org.qfox.jestful.json;

import java.io.IOException;
import java.io.OutputStream;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.ResponseSerializer;

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
public class JsonResponseSerializer implements ResponseSerializer {

	public String getContentType() {
		return "application/json";
	}

	public void serialize(Action action, MediaType mediaType, OutputStream out) throws IOException {
		ObjectMapper jackson = new ObjectMapper();
		jackson.writeValue(out, action.getResult());
	}

}
