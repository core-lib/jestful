package org.qfox.jestful.client.nio;

import org.qfox.jestful.client.ReusableResponse;
import org.qfox.jestful.core.Response;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by payne on 2017/4/2.
 */
public interface NioResponse extends Response, ReusableResponse {

    boolean load(ByteBuffer buffer) throws IOException;

}
