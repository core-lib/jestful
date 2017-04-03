package org.qfox.jestful.client.nio;

import org.qfox.jestful.core.Request;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by payne on 2017/4/2.
 */
public interface NioRequest extends Request {

    boolean copy(ByteBuffer buffer) throws IOException;

    boolean move(int n) throws IOException;

}
