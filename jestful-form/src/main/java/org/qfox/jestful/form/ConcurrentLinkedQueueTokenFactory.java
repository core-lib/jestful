package org.qfox.jestful.form;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yangchangpei on 17/8/17.
 */
public class ConcurrentLinkedQueueTokenFactory implements TokenFactory {
    private Queue<ReusableToken> pool = new ConcurrentLinkedQueue<ReusableToken>();
    private AtomicInteger size = new AtomicInteger(0);
    private int maxPoolSize = 1024;

    public ConcurrentLinkedQueueTokenFactory() {
    }

    public ConcurrentLinkedQueueTokenFactory(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    @Override
    public Token produce(String key, long timeExpired) {
        ReusableToken token = pool.poll();
        if (token != null) size.getAndDecrement();
        return token != null ? token.reuse(key, timeExpired) : new ReusableToken(key, timeExpired);
    }

    @Override
    public void recover(Token token) {
        if (token == null) throw new NullPointerException();
        if (size.getAndIncrement() < maxPoolSize) pool.offer((ReusableToken) token);
        else size.getAndDecrement();
    }

}
