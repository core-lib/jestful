package org.qfox.jestful.core;

import java.io.IOException;
import java.io.OutputStream;

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
 * @date 2016年4月8日 下午5:08:58
 *
 * @since 1.0.0
 */
public interface ResponseSerializer {

	String getContentType();

	void serialize(Action action, MediaType mediaType, OutputStream out) throws IOException;

}
