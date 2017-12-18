package org.qfox.jestful.client.cache;

import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.RequestWrapper;

public class NegotiatedRequest extends RequestWrapper {

    NegotiatedRequest(Request request) {
        super(request);
    }

}
