package org.qfox.jestful.client.cache.impl;

import org.qfox.jestful.client.cache.MsgDigester;

import java.security.NoSuchAlgorithmException;

public class BasicMsgDigesterFactory implements MsgDigesterFactory {
    private final String algorithm;

    public BasicMsgDigesterFactory(String algorithm) {
        if (algorithm == null) throw new NullPointerException();
        this.algorithm = algorithm;
    }

    @Override
    public MsgDigester produce() {
        try {
            return new BasicMsgDigester(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAlgorithm() {
        return algorithm;
    }
}
