package org.qfox.jestful.form;

/**
 * Created by yangchangpei on 17/8/17.
 */
public interface TokenFactory {

    Token produce(String key, long timeExpired);

    void recover(Token token);

}
