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
    private final ILogger logger;
    private LoggingOutputStream los;
    private boolean closed;

    LoggingRequest(Request request, ILogger logger) {
        super(request);
        this.logger = logger;
    }

    @Override
    public OutputStream getRequestOutputStream() throws IOException {
        OutputStream out = super.getRequestOutputStream();
        return los = out != null ? new LoggingOutputStream(out) : null;
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        } else {
            closed = true;
        }
        try {
            if (logger.isDebugEnabled() || logger.isInfoEnabled()) {
                String method = this.getMethod();
                String url = this.getURL();
                if (logger.isDebugEnabled()) {
                    logger.debug("{} {}", method, url);
                } else {
                    logger.info("{} {}", method, url);
                }
            }

            if (logger.isDebugEnabled()) {
                String[] headerKeys = this.getHeaderKeys();
                for (String headerKey : headerKeys) {
                    if (headerKey == null || headerKey.isEmpty()) {
                        continue;
                    }
                    String[] headerValues = this.getRequestHeaders(headerKey);
                    for (String headerValue : headerValues) {
                        logger.debug("{}: {}", headerKey, headerValue);
                    }
                }
                if (los != null) {
                    logger.debug("{}", los.toString());
                }
            }
        } finally {
            super.close();
        }
    }
}
