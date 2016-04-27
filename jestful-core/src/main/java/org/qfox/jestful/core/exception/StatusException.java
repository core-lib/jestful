package org.qfox.jestful.core.exception;

import java.io.PrintWriter;

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
public class StatusException extends JestfulException {
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

	@Override
	public void printStackTrace(PrintWriter s) {
		s.println("<html>");
		s.println("<head>");
		s.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"/>");
		s.println("<title>Error " + status + " " + message + "</title>");
		s.println("</head>");
		s.println("<body><h2>HTTP ERROR " + status + "</h2>");
		s.println("<p>Problem accessing " + uri + ". Reason:<pre>" + message + "</pre></p>");
		s.println("<hr />");
		s.println("<i><small>Powered by Jestful://</small></i>");
		s.println("<br/>");
		s.println("<br/>");
		s.println("<br/>");
		s.println("<br/>");
		s.println("<br/>");
		s.println("<br/>");
		s.println("<br/>");
		s.println("<br/>");
		s.println("<br/>");
		s.println("<br/>");
		s.println("<br/>");
		s.println("</body>");
		s.println("</html>");
	}

}
