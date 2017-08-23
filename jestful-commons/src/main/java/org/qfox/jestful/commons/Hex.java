package org.qfox.jestful.commons;

/**
 * Created by yangchangpei on 17/8/23.
 */
public class Hex {

    private static final byte[] ENCODING = new byte[]{
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
    };

    private static final byte[] DECODING = new byte[]{
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, +0, +1,
            +2, +3, +4, +5, +6, +7, +8, +9, -1, -1,
            -1, -1, -1, -1, -1, 10, 11, 12, 13, 14,
            15, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, 10, 11, 12,
            13, 14, 15, -1, -1, -1, -1, -1, -1, -1,
    };

    public static String encode(String str) {
        return new String(encode(str.getBytes()));
    }

    public static byte[] encode(byte[] str) {
        if (str == null) throw new NullPointerException();
        byte[] hex = new byte[str.length << 1];
        for (int i = 0; i < str.length; i++) {
            int h = (str[i] >> 4) & 0x0F;
            int l = str[i] & 0x0F;
            hex[i << 1] = ENCODING[h];
            hex[i << 1 | 1] = ENCODING[l];
        }
        return hex;
    }

    public static String decode(String hex) {
        return new String(decode(hex.getBytes()));
    }

    public static byte[] decode(byte[] hex) {
        if (hex == null) throw new NullPointerException();
        if ((hex.length & 1) != 0) throw new IllegalArgumentException("can not decode with odd length byte array");
        byte[] bytes = new byte[hex.length >> 1];
        for (int i = 0, j = 1; i < hex.length && j < hex.length; i += 2, j += 2) {
            int h = hex[i] > 0 && hex[i] < DECODING.length ? DECODING[hex[i]] : -1;
            int l = hex[j] > 0 && hex[j] < DECODING.length ? DECODING[hex[j]] : -1;
            if (h < 0) throw new IllegalArgumentException(hex[i] + " is not a hex byte");
            if (l < 0) throw new IllegalArgumentException(hex[j] + " is not a hex byte");
            bytes[i >> 1] = (byte) ((h << 4) | l);
        }
        return bytes;
    }

}
