package org.qfox.jestful.core.exception;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

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
 * @date 2016年3月31日 上午10:16:55
 *
 * @since 1.0.0
 */
public class StatusException extends JestfulIOException {
	private static final long serialVersionUID = 6260672121780209011L;

	protected final String uri;
	protected final String method;
	protected final int status;
	protected final String message;

	public StatusException(String uri, String method, int status, String message) {
		super();
		this.uri = uri;
		this.method = method;
		this.status = status;
		this.message = message;
	}

	public String getUri() {
		return uri;
	}

	public String getMethod() {
		return method;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public void responseTo(Writer writer) throws IOException {
		writer.append("<html>");
		writer.append("<head>");
		writer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + Charset.defaultCharset().name() + "\"/>");
		writer.append("<title>Error " + status + " " + message + "</title>");
		writer.append("</head>");
		writer.append("<body><h2>HTTP ERROR " + status + "</h2>");
		writer.append("<p>Problem accessing " + uri + ". Reason:<pre>" + message + "</pre></p>");
		writer.append("<hr />");
		writer.append("<i><small>Powered by Jestful://</small></i>");
		writer.append("<br/>");
		writer.append("<br/>");
		writer.append("<br/>");
		writer.append("<br/>");
		writer.append("<br/>");
		writer.append("<br/>");
		writer.append("<br/>");
		writer.append("<br/>");
		writer.append("<br/>");
		writer.append("<br/>");
		writer.append("<br/>");
		writer.append("</body>");
		writer.append("</html>");
	}

}
