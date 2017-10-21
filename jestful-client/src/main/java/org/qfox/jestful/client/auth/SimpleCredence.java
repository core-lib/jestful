package org.qfox.jestful.client.auth;

/**
 * Created by yangchangpei on 17/10/21.
 */
public class SimpleCredence implements Credence {
    private final Principal principal;
    private final String password;

    public SimpleCredence(String credentials) {
        int index = credentials.indexOf(':');
        if (index < 0) throw new IllegalArgumentException("malformed credentials");
        String username = credentials.substring(0, index);
        String password = credentials.substring(index + 1, credentials.length());
        this.principal = new SimplePrincipal(username);
        this.password = password;
    }

    public SimpleCredence(String username, String password) {
        this(new SimplePrincipal(username), password);
    }

    public SimpleCredence(Principal principal, String password) {
        this.principal = principal;
        this.password = password;
    }

    @Override
    public Principal getPrincipal() {
        return principal;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleCredence)) return false;

        SimpleCredence that = (SimpleCredence) o;

        if (principal != null ? !principal.equals(that.principal) : that.principal != null) return false;
        return password != null ? password.equals(that.password) : that.password == null;
    }

    @Override
    public int hashCode() {
        int result = principal != null ? principal.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return principal + ":" + password;
    }
}
