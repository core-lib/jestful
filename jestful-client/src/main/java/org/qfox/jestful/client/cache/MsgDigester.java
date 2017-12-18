package org.qfox.jestful.client.cache;

import java.io.IOException;
import java.io.InputStream;

public interface MsgDigester {

    byte[] digest(byte[] data);

    byte[] digest(InputStream in) throws IOException;

}
