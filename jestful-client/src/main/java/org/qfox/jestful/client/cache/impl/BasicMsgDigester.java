package org.qfox.jestful.client.cache.impl;

import org.qfox.jestful.client.cache.MsgDigester;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.io.DigestOutputStream;
import org.qfox.jestful.commons.io.NoneOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BasicMsgDigester implements MsgDigester {
    private final MessageDigest digest;

    public BasicMsgDigester(String algorithm) throws NoSuchAlgorithmException {
        this(MessageDigest.getInstance(algorithm));
    }

    public BasicMsgDigester(MessageDigest digest) {
        if (digest == null) throw new NullPointerException();
        this.digest = digest;
    }

    @Override
    public byte[] digest(byte[] data) {
        return digest.digest(data);
    }

    @Override
    public byte[] digest(InputStream in) throws IOException {
        DigestOutputStream dos = new DigestOutputStream(new NoneOutputStream(), digest);
        IOKit.transfer(in, dos);
        return dos.digest();
    }
}
