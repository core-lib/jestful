package org.qfox.jestful.server;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

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
    private JestfulHeadResponseServletOutputStream out;
    private PrintWriter writer;

    public JestfulHeadResponse(HttpServletResponse response) {
        super(response);
    }

    public void finish() {
        super.setContentLength(out != null ? out.size : 0);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (out == null) {
            out = new JestfulHeadResponseServletOutputStream();
        }
        return out;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer == null) {
            String charset = getCharacterEncoding();
            ServletOutputStream sos = getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(sos, charset);
            writer = new PrintWriter(osw, true);
        }
        return writer;
    }

    private static class JestfulHeadResponseServletOutputStream extends ServletOutputStream {
        private int size = 0;

        @Override
        public void write(int b) {
            size++;
        }

        @Override
        public void write(byte[] b) throws IOException {
            size += b.length;
        }

        @Override
        public void write(byte buf[], int offset, int len) throws IOException {
            size += len;
        }

    }

}
