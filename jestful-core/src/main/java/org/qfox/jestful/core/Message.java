package org.qfox.jestful.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>
 * Description: Network Protocol Message
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月8日 下午3:49:21
 *
 * @since 1.0.0
 */
public interface Message {

	String[] getHeaders();

	String getHeader(String name);

	String[] getHeaders(String name);

	InputStream getInputStream() throws IOException;

	OutputStream getOutputStream() throws IOException;

}
