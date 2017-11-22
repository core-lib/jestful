package org.qfox.jestful.client.redirect;

import java.io.Serializable;

public class Redirection implements Serializable {
    private static final long serialVersionUID = 6643806687815107747L;

    private final String method;
    private final String URL;

    public Redirection(String method, String URL) {
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

        if (method != null ? !method.equals(that.method) : that.method != null) return false;
        return URL != null ? URL.equals(that.URL) : that.URL == null;
    }

    @Override
    public int hashCode() {
        int result = method != null ? method.hashCode() : 0;
        result = 31 * result + (URL != null ? URL.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return method + " " + URL;
    }
}
