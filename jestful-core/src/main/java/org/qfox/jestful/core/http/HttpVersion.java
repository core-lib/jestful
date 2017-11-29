package org.qfox.jestful.core.http;

import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.core.Version;

import java.util.StringTokenizer;

public class HttpVersion implements Version {
    private final int major;
    private final int minor;

    public HttpVersion(int major, int minor) {
        if (major < 0) throw new IllegalArgumentException("http major version " + major + " < 0");
        if (minor < 0) throw new IllegalArgumentException("http minor version " + minor + " < 0");
        this.major = major;
        this.minor = minor;
    }

    public static HttpVersion valueOf(String version) {
        if (StringKit.isEmpty(version)) throw new IllegalArgumentException("http version string must not be null or empty");
        try {
            StringTokenizer tokenizer = new StringTokenizer(version, ".");
            String major = tokenizer.nextToken();
            String minor = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : "0";
            return new HttpVersion(Integer.valueOf(major), Integer.valueOf(minor));
        } catch (Exception e) {
            throw new IllegalArgumentException("malformed http version string:" + version, e);
        }
    }

    @Override
    public int compareTo(Version o) {
        if (o == null) throw new NullPointerException();
        if (o.getClass() != this.getClass()) throw new IllegalArgumentException("can not compare with a different type version:" + o);
        HttpVersion that = (HttpVersion) o;
        return this.major > that.major ? 1 : this.major == that.major ? this.minor > that.minor ? 1 : this.minor == that.minor ? 0 : -1 : -1;
    }

    @Override
    public int getMajor() {
        return major;
    }

    @Override
    public int getMinor() {
        return minor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpVersion that = (HttpVersion) o;
        return major == that.major && minor == that.minor;
    }

    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + minor;
        return result;
    }

    @Override
    public String toString() {
        return major + "." + minor;
    }
}
