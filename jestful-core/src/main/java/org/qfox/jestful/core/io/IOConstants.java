package org.qfox.jestful.core.io;

import java.nio.ByteBuffer;

/**
 * Created by yangchangpei on 17/3/25.
 */
public interface IOConstants {
    int EOF = -1;
    char[] CRLF = new char[]{'\r', '\n'};
    char[] SPRT = new char[]{':', ' '};
    ByteBuffer EMPTY = ByteBuffer.allocate(0);

}
