package org.qfox.jestful.form;

import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

/**
 * Created by yangchangpei on 17/8/21.
 */
public class RedisTokenManager implements TokenManager {
    private Jedis jedis = new Jedis("127.0.0.1", 6379);
    private long duration = 30L * 60L * 1000L; // 半小时失效
    private TimeUnit unit = TimeUnit.MILLISECONDS;
    private TokenGenerator generator = new UUIDTokenGenerator();
    private String prefix = "form-";
    private String suffix = ".token";

    @Override
    public String grant() throws TokenExceedException {
        return grant(duration, unit);
    }

    @Override
    public String grant(long duration, TimeUnit unit) throws TokenExceedException {
        if (duration <= 0) throw new IllegalArgumentException("duration must greater than zero");
        if (unit == null) throw new NullPointerException("unit can not be null");
        String key = generator.generate();
        long milliseconds = unit.toMillis(duration);
        String token = prefix + key + suffix;
        String value = String.valueOf(System.currentTimeMillis() + milliseconds);
        jedis.psetex(token, milliseconds, value);
        return key;
    }

    @Override
    public void verify(String key) throws TokenExpiredException, TokenMissedException {
        String token = prefix + key + suffix;
        Long count = jedis.del(token);
        if (count == 0) throw new TokenMissedException(key);
    }

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
