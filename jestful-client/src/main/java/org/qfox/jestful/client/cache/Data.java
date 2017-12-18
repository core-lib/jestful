package org.qfox.jestful.client.cache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Data {

    void read(Reader reader) throws IOException;

    void write(Writer writer) throws IOException;

    void free() throws IOException;

    interface Reader {
        void read(InputStream in) throws IOException;
    }

    interface Writer {
        void write(OutputStream out) throws IOException;
    }

}
