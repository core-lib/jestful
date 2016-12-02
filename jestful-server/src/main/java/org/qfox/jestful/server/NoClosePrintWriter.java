package org.qfox.jestful.server;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * Created by yangchangpei on 16/12/2.
 */
public class NoClosePrintWriter extends PrintWriter {

    public NoClosePrintWriter(Writer out) {
        super(out);
    }

    public NoClosePrintWriter(Writer out, boolean autoFlush) {
        super(out, autoFlush);
    }

    @Override
    public void close() {

    }

}
