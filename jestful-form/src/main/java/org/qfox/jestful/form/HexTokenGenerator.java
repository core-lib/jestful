package org.qfox.jestful.form;

import org.qfox.jestful.commons.Hex;

/**
 * Created by yangchangpei on 17/8/23.
 */
public class HexTokenGenerator implements TokenGenerator {
    private TokenGenerator tokenGenerator = new UUIDTokenGenerator();

    @Override
    public String generate() {
        return Hex.encode(tokenGenerator.generate());
    }

}
