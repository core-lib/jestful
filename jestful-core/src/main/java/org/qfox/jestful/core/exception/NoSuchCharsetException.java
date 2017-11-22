package org.qfox.jestful.core.exception;

import org.qfox.jestful.core.Charsets;

public class NoSuchCharsetException extends JestfulException {
    private static final long serialVersionUID = -1840352004851516851L;

    private final Charsets expects;
    private final Charsets actuals;

    public NoSuchCharsetException(Charsets expects, Charsets actuals) {
        super("can not find the acceptable charsets cause you specified accepting charsets [" + expects + "] but the system only serializable [" + actuals + "]");
        this.expects = expects;
        this.actuals = actuals;
    }

    public Charsets getExpects() {
        return expects;
    }

    public Charsets getActuals() {
        return actuals;
    }

}
