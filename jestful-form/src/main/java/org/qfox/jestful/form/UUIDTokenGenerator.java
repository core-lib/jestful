package org.qfox.jestful.form;

import java.util.UUID;

/**
 * Created by yangchangpei on 17/8/17.
 */
public class UUIDTokenGenerator implements TokenGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
