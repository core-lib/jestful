package org.qfox.jestful.client.cache.impl;

import org.qfox.jestful.client.cache.StrEncoder;
import org.qfox.jestful.commons.Hex;

public class HexStrEncoder implements StrEncoder {

    @Override
    public String encode(byte[] bytes) {
        return new String(Hex.encode(bytes));
    }
}
