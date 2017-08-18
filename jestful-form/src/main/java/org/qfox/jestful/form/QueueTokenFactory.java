package org.qfox.jestful.form;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yangchangpei on 17/8/17.
 */
public class QueueTokenFactory implements TokenFactory {
    private final Queue<Token> queue = new ConcurrentLinkedQueue<Token>();
    private final AtomicInteger size = new AtomicInteger(0);
    private int capacity = 1024;

    @Override
    public Token produce(String key, long timeExpired) {
        Token token = queue.poll();
        if (token != null) size.getAndDecrement();
        return token != null ? token.reuse(key, timeExpired) : new Token(key, timeExpired);
    }

    @Override
    public void recover(Token token) {
        if (token == null) throw new NullPointerException();
        if (size.getAndIncrement() < capacity) queue.offer(token);
        else size.getAndDecrement();
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
