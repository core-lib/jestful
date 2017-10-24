package org.qfox.jestful.client.auth;

import org.qfox.jestful.core.exception.JestfulException;

/**
 * Created by yangchangpei on 17/10/24.
 */
public class AuthenticationException extends JestfulException {
    private static final long serialVersionUID = -5124137487917960868L;

    public AuthenticationException() {
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}
