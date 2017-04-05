package org.qfox.jestful.client.aio;

import org.qfox.jestful.core.Request;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by yangchangpei on 17/4/5.
 */
public interface AioRequest extends Request {

    void copy(ByteBuffer buffer) throws IOException;

    boolean move(int n) throws IOException;

}
