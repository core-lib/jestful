package org.qfox.jestful.client.nio.connection;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by yangchangpei on 17/4/2.
 */
public interface NioSSLChannel {

    void write(ByteBuffer buffer) throws IOException;

    void read(ByteBuffer buffer) throws IOException;

    void copy(ByteBuffer buffer) throws IOException;

    void move(int n) throws IOException;

    void load(ByteBuffer buffer) throws IOException;

}
