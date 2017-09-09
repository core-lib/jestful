package org.qfox.jestful.cache;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangchangpei on 17/9/9.
 */
public class Period implements Serializable {
    private static final long serialVersionUID = -3907614579953566038L;

    private TimeUnit unit;
    private long duration;

    public Period() {
    }

    public Period(TimeUnit unit, long duration) {
        this.unit = unit;
        this.duration = duration;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
