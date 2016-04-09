package org.qfox.jestful.core;

import java.io.IOException;
import java.io.InputStream;

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
 * @date 2016年4月8日 下午5:21:07
 *
 * @since 1.0.0
 */
public interface Deserializer {

	String getContentType();

	void deserialize(Action action, MediaType mediaType, InputStream in) throws IOException;

}
