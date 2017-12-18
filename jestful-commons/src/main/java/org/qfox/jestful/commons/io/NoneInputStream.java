package org.qfox.jestful.commons.io;

import java.io.IOException;
import java.io.InputStream;

public class NoneInputStream extends InputStream {

    @Override
    public int read() throws IOException {
        return -1;
    }
}
