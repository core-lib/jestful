package org.qfox.jestful.client.redirect.exception;

import org.qfox.jestful.client.redirect.Redirections;
import org.qfox.jestful.core.exception.JestfulException;

public class RedirectOverloadException extends JestfulException {
    private static final long serialVersionUID = 3715158781599429696L;

    private final Redirections redirections;

    public RedirectOverloadException(Redirections redirections) {
        super("too many redirects");
        this.redirections = redirections;
    }

    public Redirections getRedirections() {
        return redirections;
    }
}
