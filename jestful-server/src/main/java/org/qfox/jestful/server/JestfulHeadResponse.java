package org.qfox.jestful.server;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年6月1日 下午10:03:56
 * @since 1.0.0
 */
public class JestfulHeadResponse extends HttpServletResponseWrapper {
    private ServletOutputStream servletOutputStream;
    private PrintWriter printWriter;

    public JestfulHeadResponse(HttpServletResponse response) {
        super(response);
    }

    public void finish() throws Exception {
        Integer contentLength = 0;
        if (servletOutputStream != null) {
            Method method = servletOutputStream.getClass().getMethod("getContentLength");
            method.setAccessible(true);
            contentLength = (Integer) method.invoke(servletOutputStream);
        }
        this.setContentLength(contentLength);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (servletOutputStream == null) {
            try {
                servletOutputStream = (ServletOutputStream) Class.forName("javax.servlet.http.NoBodyOutputStream").newInstance();
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
        return servletOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (printWriter == null) {
            String charset = getCharacterEncoding();
            ServletOutputStream sos = getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(sos, charset);
            printWriter = new PrintWriter(osw, true);
        }
        return printWriter;
    }

}
