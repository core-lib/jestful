package org.qfox.jestful.client.aio;

import org.qfox.jestful.core.Response;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by yangchangpei on 17/4/5.
 */
public interface AioResponse extends Response {

    boolean load(ByteBuffer buffer) throws IOException;

}
