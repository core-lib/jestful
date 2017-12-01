package org.qfox.jestful.commons.clock;

import java.util.concurrent.TimeUnit;

/**
 * Created by Payne on 2017/12/1.
 */
public class Schedule implements Comparable<Schedule> {
    private final long time;
    private final Execution execution;
    private volatile Schedule next;

    Schedule(long time, Execution execution) {
        this.time = time;
        this.execution = execution;
    }

    Schedule(long time, Execution execution, Schedule next) {
        this.time = time;
        this.execution = execution;
        this.next = next;
    }

    void schedule(Executor executor) {
        executor.execute(execution);
    }

    Schedule next() {
        return this.next;
    }

    void next(Schedule next) {
        if (next == null) throw new NullPointerException();
        this.next = next;
    }

    long delay() {
        return time - System.currentTimeMillis();
    }

    long delay(TimeUnit unit) {
        if (unit == null) throw new NullPointerException();
        return unit.convert(delay(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Schedule that) {
        return this.time > that.time ? 1 : this.time < that.time ? -1 : 0;
    }
}
