package org.qfox.jestful.commons.io;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;

/**
 * Created by yangchangpei on 17/10/26.
 */
public class DigestOutputStream extends OutputStream {
    private final OutputStream out;
    private final MessageDigest digest;

    public DigestOutputStream(OutputStream out, String algorithm) throws NoSuchAlgorithmException {
        this(out, MessageDigest.getInstance(algorithm));
    }

    public DigestOutputStream(OutputStream out, String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
        this(out, MessageDigest.getInstance(algorithm, provider));
    }

    public DigestOutputStream(OutputStream out, String algorithm, Provider provider) throws NoSuchAlgorithmException, NoSuchProviderException {
        this(out, MessageDigest.getInstance(algorithm, provider));
    }

    public DigestOutputStream(OutputStream out, MessageDigest digest) {
        if (out == null) throw new NullPointerException("out");
        if (digest == null) throw new NullPointerException("digest");
        this.out = out;
        this.digest = digest;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
        digest.update((byte) b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        out.write(b);
        digest.update(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
        digest.update(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

    public byte[] digest() {
        return digest.digest();
    }

}
