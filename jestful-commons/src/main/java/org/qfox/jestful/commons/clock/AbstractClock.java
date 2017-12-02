package org.qfox.jestful.commons.clock;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Payne on 2017/12/2.
 */
public abstract class AbstractClock implements Clock {

    @Override
    public void apply(Execution execution, Date date) {
        if (date == null) throw new NullPointerException("date == null");
        apply(execution, date.getTime());
    }

    @Override
    public void apply(Execution execution, long time) {
        apply(execution, time - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }


}
