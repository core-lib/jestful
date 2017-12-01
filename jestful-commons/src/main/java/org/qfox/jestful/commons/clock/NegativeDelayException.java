package org.qfox.jestful.commons.clock;

/**
 * Created by Payne on 2017/12/1.
 */
public class NegativeDelayException extends RuntimeException {
    private static final long serialVersionUID = -2885603228282842716L;

    public NegativeDelayException() {
    }

    public NegativeDelayException(String message) {
        super(message);
    }

    public NegativeDelayException(String message, Throwable cause) {
        super(message, cause);
    }

    public NegativeDelayException(Throwable cause) {
        super(cause);
    }
}
