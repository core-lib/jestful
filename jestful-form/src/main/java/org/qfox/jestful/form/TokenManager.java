package org.qfox.jestful.form;

import java.util.concurrent.TimeUnit;

/**
 * Created by yangchangpei on 17/8/17.
 */
public interface TokenManager {

    String grant() throws TokenExceedException;

    String grant(long duration, TimeUnit unit) throws TokenExceedException;

    void verify(String key) throws TokenExpiredException, TokenMissedException;

}
