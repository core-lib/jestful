package org.qfox.jestful.logging;

import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.ResponseWrapper;
import org.qfox.jestful.core.Status;

import java.io.IOException;
import java.io.InputStream;

/**
 * 日志包装回应
 *
 * @author Payne 646742615@qq.com
 * 2019/8/22 15:25
 */
public class LoggingResponse extends ResponseWrapper {
    private final ILogger logger;
    private LoggingInputStream lis;
    private boolean closed;

    LoggingResponse(Response response, ILogger logger) {
        super(response);
        this.logger = logger;
    }

    @Override
    public InputStream getResponseInputStream() throws IOException {
        InputStream in = super.getResponseInputStream();
        return lis = in != null ? new LoggingInputStream(in) : null;
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
                Status status = this.getResponseStatus();
                if (logger.isDebugEnabled()) {
                    logger.debug("{}", status);
                } else {
                    logger.info("{}", status);
                }
            }
            if (logger.isDebugEnabled()) {
                String[] headerKeys = this.getHeaderKeys();
                for (String headerKey : headerKeys) {
                    if (headerKey == null) {
                        continue;
                    }
                    String[] headerValues = this.getResponseHeaders(headerKey);
                    for (String headerValue : headerValues) {
                        logger.debug("{}: {}", headerKey, headerValue);
                    }
                }
                if (lis != null) {
                    logger.debug("{}", lis.toString());
                }
            }
            if (logger.isWarnEnabled()) {
                String warning = this.getResponseHeader("Warning");
                if (!StringKit.isEmpty(warning)) {
                    logger.warn(warning);
                }
            }
        } finally {
            super.close();
        }
    }
}
