package org.qfox.jestful.client.exception;

import org.qfox.jestful.core.exception.JestfulRuntimeException;

public class UnsupportedProtocolException extends JestfulRuntimeException {
    private static final long serialVersionUID = 4344774770083264782L;

    private final String protocol;

    public UnsupportedProtocolException(String protocol) {
        this("unsupported protocol " + protocol, protocol);
    }

    public UnsupportedProtocolException(String message, Throwable cause, String protocol) {
        super(message, cause);
        this.protocol = protocol;
    }

    public UnsupportedProtocolException(String message, String protocol) {
        super(message);
        this.protocol = protocol;
    }

    public UnsupportedProtocolException(Throwable cause, String protocol) {
        super(cause);
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }
}
