package org.qfox.jestful.commons;

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

}
