package org.qfox.jestful.client.cache.impl;

import org.qfox.jestful.client.cache.MsgDigester;

import java.io.IOException;
import java.io.InputStream;

public class SynchronizedMsgDigester implements MsgDigester {
    private final MsgDigester digester;

    public SynchronizedMsgDigester(MsgDigester digester) {
        if (digester == null) throw new NullPointerException();
        this.digester = digester;
    }

    @Override
    public synchronized byte[] digest(byte[] data) {
        return digester.digest(data);
    }

    @Override
    public synchronized byte[] digest(InputStream in) throws IOException {
        return digester.digest(in);
    }
}
