package org.qfox.jestful.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yangchangpei on 17/8/17.
 */
public class ConcurrentHashMapTokenManager implements TokenManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private long duration = 30L * 60L * 1000L; // 半小时失效
    private TimeUnit unit = TimeUnit.MILLISECONDS;
    private TokenGenerator generator = new UUIDTokenGenerator();
    private TokenFactory factory = new ConcurrentLinkedQueueTokenFactory();
    private Map<String, Token> map = new ConcurrentHashMap<String, Token>();
    private AtomicInteger size = new AtomicInteger(0);
    private int maxBuffSize;
    private long cleanInterval = 5L * 1000L;

    private Thread cleaner = new Thread() {

        {
            this.setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(cleanInterval);
                    Iterator<Map.Entry<String, Token>> iterator = map.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, Token> entry = iterator.next();
                        if (entry.getValue().expired()) {
                            iterator.remove();
                        }
                    }

                } catch (Exception e) {
                    logger.warn("error occur when cleaning form tokens:", e);
                }
            }
        }

    };

    public ConcurrentHashMapTokenManager() {
        this(2048);
    }

    public ConcurrentHashMapTokenManager(int maxBuffSize) {
        this.maxBuffSize = maxBuffSize;
        this.cleaner.start();
    }

    @Override
    public String create() throws TokenExceedException {
        return create(duration, unit);
    }

    @Override
    public String create(long duration, TimeUnit unit) throws TokenExceedException {
        if (size.get() < maxBuffSize) {

            String key = generator.generate();
            long timeExpired = System.currentTimeMillis() + unit.toMillis(duration);
            Token token = factory.produce(key, timeExpired);
            map.put(key, token);
            return key;
        } else {
            throw new TokenExceedException();
        }
    }

    @Override
    public void verify(String key) throws TokenExpiredException, TokenMissedException {
        Token token = map.remove(key);
        if (token == null) throw new TokenMissedException(key);
        if (token.expired()) throw new TokenExpiredException(key);
        factory.recover(token);
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
}
