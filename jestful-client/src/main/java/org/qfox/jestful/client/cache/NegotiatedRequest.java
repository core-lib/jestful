package org.qfox.jestful.client.cache;

import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.RequestWrapper;

public class NegotiatedRequest extends RequestWrapper {
    private final long timeRequested;

    NegotiatedRequest(Request request) {
        super(request);
        this.timeRequested = System.currentTimeMillis();
    }

    public long getTimeRequested() {
        return timeRequested;
    }
}
