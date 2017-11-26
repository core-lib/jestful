package org.qfox.jestful.client.connection;

import javax.net.ssl.HttpsURLConnection;

public class JestfulHttpsClientResponse extends JestfulHttpClientResponse {

    public JestfulHttpsClientResponse(HttpsURLConnection httpsURLConnection) {
        super(httpsURLConnection);
    }

}
