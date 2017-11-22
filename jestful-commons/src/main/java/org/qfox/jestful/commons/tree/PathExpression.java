package org.qfox.jestful.commons.tree;

public class PathExpression implements Expression<PathExpression> {
    private final String path;
    private final String method;
    private final String version;

    public PathExpression() {
        this(null, null, null);
    }

    public PathExpression(String path) {
        this(path, null, null);
    }

    public PathExpression(String path, String method, String version) {
        super();
        this.path = path != null ? path : "";
        this.method = method != null ? method : "";
        this.version = version != null ? version : "";
    }

    public boolean match(String value) {
        return value.equals(path) || value.matches(path);
    }

    public boolean isEmpty() {
        return path.length() == 0;
    }

    public int compareTo(PathExpression o) {
        return path.compareTo(o.path) != 0 ? path.compareTo(o.path) : method.compareTo(o.method) != 0 ? method.compareTo(o.method) : version.compareTo(o.version);
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PathExpression other = (PathExpression) obj;
        if (method == null) {
            if (other.method != null)
                return false;
        } else if (!method.equals(other.method))
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return path;
    }

}