package org.qfox.jestful.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangchangpei on 17/8/17.
 */
public class MapTokenManager implements TokenManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<String, Token> map = new ConcurrentHashMap<String, Token>();

    private long duration = 30L * 60L * 1000L; // 半小时失效
    private TimeUnit unit = TimeUnit.MILLISECONDS;
    private TokenGenerator generator = new UUIDTokenGenerator();
    private TokenFactory factory = new QueueTokenFactory();
    private long interval = 30L * 1000L;

    public MapTokenManager() {
        this(30L * 1000L);
    }

    public MapTokenManager(long interval) {
        this.interval = interval;
        Thread cleaner = new Thread(this.new Cleaner());
        cleaner.setDaemon(true);
        cleaner.start();
    }

    @Override
    public String create() throws TokenExceedException {
        return create(duration, unit);
    }

    @Override
    public String create(long duration, TimeUnit unit) throws TokenExceedException {
        if (duration <= 0) throw new IllegalArgumentException("duration must greater than zero");
        if (unit == null) throw new NullPointerException("unit can not be null");
        String key = generator.generate();
        long timeExpired = System.currentTimeMillis() + unit.toMillis(duration);
        Token token = factory.produce(key, timeExpired);
        map.put(key, token);
        return key;
    }

    @Override
    public void verify(String key) throws TokenExpiredException, TokenMissedException {
        if (key == null) throw new NullPointerException("key can not be null");
        Token token = map.remove(key);
        if (token == null) throw new TokenMissedException(key);
        factory.recover(token);
        if (token.expired()) throw new TokenExpiredException(token.getKey(), token.getTimeExpired());
    }

    private class Cleaner implements Runnable {
        private boolean loop = true;

        @Override
        public void run() {
            while (loop) {
                try {
                    Thread.sleep(interval);
                    Iterator<Map.Entry<String, Token>> iterator = map.entrySet().iterator();
                    while (iterator.hasNext()) if (iterator.next().getValue().expired()) iterator.remove();
                } catch (Exception e) {
                    logger.warn("exception occur while cleaning expired tokens", e);
                }
            }
        }

    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public TokenGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(TokenGenerator generator) {
        this.generator = generator;
    }

    public TokenFactory getFactory() {
        return factory;
    }

    public void setFactory(TokenFactory factory) {
        this.factory = factory;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}
