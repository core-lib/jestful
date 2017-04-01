package org.qfox.jestful.client.nio;

import org.qfox.jestful.core.Response;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by payne on 2017/4/2.
 */
public interface NioResponse extends Response {

    boolean receive(ByteBuffer buffer) throws IOException;

}
