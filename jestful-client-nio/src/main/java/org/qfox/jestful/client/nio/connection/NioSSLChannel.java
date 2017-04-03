package org.qfox.jestful.client.nio.connection;

import javax.net.ssl.SSLEngineResult;
import java.io.IOException;

/**
 * Created by yangchangpei on 17/4/2.
 */
public interface NioSSLChannel {

    void doHandshake() throws IOException;

}
