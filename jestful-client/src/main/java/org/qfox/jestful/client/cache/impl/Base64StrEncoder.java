package org.qfox.jestful.client.cache.impl;

import org.qfox.jestful.client.cache.StrEncoder;
import org.qfox.jestful.commons.Base64;

public class Base64StrEncoder implements StrEncoder {

    @Override
    public String encode(byte[] bytes) {
        return new String(Base64.encode(bytes));
    }
}
