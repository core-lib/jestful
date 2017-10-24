package org.qfox.jestful.commons;

import java.nio.charset.Charset;

/**
 * Created by yangchangpei on 17/9/29.
 */
public class StringKit {

    public static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }

    public static boolean isEqual(String a, String b) {
        return isEqual(a, b, false);
    }

    public static boolean isEqual(String a, String b, boolean isCaseIgnored) {
        return a == null ? b == null : isCaseIgnored ? a.equalsIgnoreCase(b) : a.equals(b);
    }

    public static String concat(String... values) {
        if (values == null || values.length == 0) return null;
        final StringBuilder sb = new StringBuilder();
        for (String value : values) sb.append(value);
        return sb.toString();
    }

    public static String concat(char delimiter, String... values) {
        if (values == null || values.length == 0) return null;
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            sb.append(values[i]);
            if (i < values.length - 1) sb.append(delimiter);
        }
        return sb.toString();
    }

    public static byte[] bytes(String value) {
        if (value == null) throw new NullPointerException("value is null");
        return value.getBytes();
    }

    public static byte[] bytes(String value, Charset charset) {
        if (value == null) throw new NullPointerException("value is null");
        if (charset == null) throw new NullPointerException("charset is null");
        return value.getBytes(charset);
    }

}
