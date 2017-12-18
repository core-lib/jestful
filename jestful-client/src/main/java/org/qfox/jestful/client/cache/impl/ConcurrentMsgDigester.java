package org.qfox.jestful.client.cache.impl;

import org.qfox.jestful.client.cache.MsgDigester;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentMsgDigester implements MsgDigester {
    private final Queue<MsgDigester> queue = new ConcurrentLinkedQueue<MsgDigester>();
    private final MsgDigesterFactory factory;

    public ConcurrentMsgDigester(MsgDigesterFactory factory) {
        if (factory == null) throw new NullPointerException();
        this.factory = factory;
    }

    @Override
    public byte[] digest(byte[] data) {
        MsgDigester digester = queue.poll();
        try {
            if (digester == null) digester = factory.produce();
            if (digester == null) throw new NullPointerException();
            return digester.digest(data);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (digester != null) queue.offer(digester);
        }
    }

    @Override
    public byte[] digest(InputStream in) throws IOException {
        MsgDigester digester = queue.poll();
        try {
            if (digester == null) digester = factory.produce();
            if (digester == null) throw new NullPointerException();
            return digester.digest(in);
        } catch (RuntimeException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (digester != null) queue.offer(digester);
        }
    }
}
