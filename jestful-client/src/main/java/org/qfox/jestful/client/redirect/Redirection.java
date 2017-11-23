package org.qfox.jestful.client.redirect;

import java.io.Serializable;

public class Redirection implements Serializable {
    private static final long serialVersionUID = 6643806687815107747L;

    private final String method;
    private final String URL;

    public Redirection(String method, String URL) {
        if (method == null) throw new IllegalArgumentException("method == null");
        if (URL == null) throw new IllegalArgumentException("method == null");
        this.method = method;
        this.URL = URL;
    }

    public String getMethod() {
        return method;
    }

    public String getURL() {
        return URL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Redirection that = (Redirection) o;

        return method.equals(that.method) && URL.equals(that.URL);
    }

    @Override
    public int hashCode() {
        int result = method.hashCode();
        result = 31 * result + URL.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return method + " " + URL;
    }
}
