package org.qfox.jestful.commons;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

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

    public static boolean isEquals(String a, String b) {
        return isEquals(a, b, false);
    }

    public static boolean isEquals(String a, String b, boolean isCaseIgnored) {
        return a == null ? b == null : isCaseIgnored ? a.equalsIgnoreCase(b) : a.equals(b);
    }

    public static boolean isDiffer(String a, String b) {
        return !isEquals(a, b);
    }

    public static boolean isDiffer(String a, String b, boolean isCaseIgnored) {
        return !isEquals(a, b, isCaseIgnored);
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

    public static byte[] md5(String text) {
        return md5(text, "UTF-8");
    }

    public static byte[] md5(String text, String charset) {
        return md5(text, Charset.forName(charset));
    }

    public static byte[] md5(String text, Charset charset) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = bytes(text, charset);
            return md5.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5Hex(String text) {
        return md5Hex(text, "UTF-8");
    }

    public static String md5Hex(String text, String charset) {
        return md5Hex(text, Charset.forName(charset));
    }

    public static String md5Hex(String text, Charset charset) {
        byte[] md5 = md5(text, charset);
        byte[] hex = Hex.encode(md5);
        return new String(hex);
    }

    public static String random(int length) {
        if (length <= 0) throw new IllegalArgumentException("length must greater than zero");
        String base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder nonce = new StringBuilder();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < length; i++) {
            int bound = base.length();
            int index = random.nextInt(bound);
            nonce.append(base.charAt(index));
        }
        return nonce.toString();
    }

}
