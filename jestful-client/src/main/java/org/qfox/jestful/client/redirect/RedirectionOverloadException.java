package org.qfox.jestful.client.redirect;

import org.qfox.jestful.core.exception.JestfulException;

public class RedirectionOverloadException extends JestfulException {
    private static final long serialVersionUID = 3715158781599429696L;

    private final Redirections redirections;

    public RedirectionOverloadException(Redirections redirections) {
        super("too many redirects");
        this.redirections = redirections;
    }

    public Redirections getRedirections() {
        return redirections;
    }
}
