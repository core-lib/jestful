package org.qfox.jestful.logging;

import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.RequestWrapper;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 日志包装请求
 *
 * @author Payne 646742615@qq.com
 * 2019/8/22 15:24
 */
public class LoggingRequest extends RequestWrapper {

    LoggingRequest(Request request) {
        super(request);
    }

    @Override
    public OutputStream getRequestOutputStream() throws IOException {
        OutputStream out = super.getRequestOutputStream();
        return new LoggingOutputStream(out, this);
    }
}
