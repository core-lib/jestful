package org.qfox.jestful.core;

import java.io.IOException;
import java.io.InputStream;
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
 * @date 2016年4月12日 下午8:54:46
 *
 * @since 1.0.0
 */
public interface Request {

	String[] getHeaderKeys();

	String getRequestHeader(String name);

	void setRequestHeader(String name, String value);

	String[] getRequestHeaders(String name);

	void setRequestHeaders(String name, String[] values);

	int getConnectTimeout();

	void setConnectTimeout(int timeout);

	int getTransferTimeout();

	void setTransferTimeout(int timeout);

	void connect() throws IOException;

	InputStream getRequestInputStream() throws IOException;

	OutputStream getRequestOutputStream() throws IOException;

}
