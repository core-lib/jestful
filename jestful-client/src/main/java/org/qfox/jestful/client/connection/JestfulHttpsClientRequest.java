package org.qfox.jestful.client.connection;

import javax.net.ssl.HttpsURLConnection;

public class JestfulHttpsClientRequest extends JestfulHttpClientRequest {

    public JestfulHttpsClientRequest(HttpsURLConnection httpsURLConnection) {
        super(httpsURLConnection);
    }

}
