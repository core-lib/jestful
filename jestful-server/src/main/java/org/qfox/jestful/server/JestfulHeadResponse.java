package org.qfox.jestful.server;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;

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
public class JestfulHeadResponse extends HttpServletResponseWrapper implements InvocationHandler {
    private ServletOutputStream servletOutputStream;
    private ServletOutputStream enhanceOutputStream;
    private PrintWriter printWriter;
    private int contentLength = 0;

    public JestfulHeadResponse(HttpServletResponse response) {
        super(response);
    }

    public void finish() {
        super.setContentLength(contentLength);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (enhanceOutputStream == null) {
            servletOutputStream = super.getOutputStream();
            enhanceOutputStream = (ServletOutputStream) Enhancer.create(ServletOutputStream.class, new Class<?>[0], this);
        }
        return enhanceOutputStream;
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

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        if ("write".equals(method.getName()) && method.getParameterTypes().length == 1 && method.getParameterTypes()[0] == int.class) {
            contentLength++;
            return null;
        }
        if ("write".equals(method.getName()) && method.getParameterTypes().length == 1 && method.getParameterTypes()[0] == byte[].class) {
            byte[] b = (byte[]) args[0];
            contentLength += b.length;
            return null;
        }
        if ("write".equals(method.getName()) && method.getParameterTypes().length == 3 && method.getParameterTypes()[2] == int.class) {
            Integer len = (Integer) args[2];
            contentLength += len;
            return null;
        }
        return method.invoke(servletOutputStream, args);
    }

}
