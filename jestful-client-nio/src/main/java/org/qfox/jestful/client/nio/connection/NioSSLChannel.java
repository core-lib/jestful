package org.qfox.jestful.client.nio.connection;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by yangchangpei on 17/4/2.
 */
public interface NioSSLChannel {

    boolean wrap(ByteBuffer from, ByteBuffer to) throws IOException;

    boolean unwrap(ByteBuffer from, ByteBuffer to) throws IOException;

}
