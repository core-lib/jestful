package org.qfox.jestful.interception;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Payne on 2017/5/6.
 */
public class Interceptors extends ArrayList<Interceptor> implements Comparable<Interceptors> {

    public Interceptors(Interceptor... interceptors) {
        super(Arrays.asList(interceptors));
    }

    @Override
    public int compareTo(Interceptors o) {
        return 0;
    }
}
