package org.qfox.jestful.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.io.IOUtils;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.ResponseSerializer;

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
 * @date 2016年4月9日 下午3:33:36
 *
 * @since 1.0.0
 */
public class JavaResponseSerializer implements ResponseSerializer {

	public String getContentType() {
		return "application/x-java-serialized-object";
	}

	public void serialize(Action action, MediaType mediaType, OutputStream out) throws IOException {
		ObjectOutputStream oos = null;
		try {
			Object result = action.getResult();
			oos = new ObjectOutputStream(out);
			oos.writeObject(result);
		} finally {
			IOUtils.close(oos);
		}
	}

}
