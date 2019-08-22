package org.qfox.jestful.logging;

import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.ResponseWrapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * 日志包装回应
 *
 * @author Payne 646742615@qq.com
 * 2019/8/22 15:25
 */
public class LoggingResponse extends ResponseWrapper {

    LoggingResponse(Response response) {
        super(response);
    }

    @Override
    public InputStream getResponseInputStream() throws IOException {
        InputStream in = super.getResponseInputStream();
        return new LoggingInputStream(in, this);
    }
}
