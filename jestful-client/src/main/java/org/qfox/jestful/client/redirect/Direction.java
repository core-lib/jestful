package org.qfox.jestful.client.redirect;

public class Direction {
    private final String method;
    private final String URL;

    public Direction(String method, String URL) {
        if (method == null) throw new IllegalArgumentException("method == null");
        if (URL == null) throw new IllegalArgumentException("method == null");
        this.method = method;
        this.URL = URL;
    }

    public boolean equals(Redirection redirection) {
        return method.equals(redirection.getMethod()) && URL.equals(redirection.getURL());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Direction direction = (Direction) o;

        return method.equals(direction.method) && URL.equals(direction.URL);
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
