package org.qfox.jestful.commons.clock;

import org.qfox.jestful.commons.Destructible;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Payne on 2017/12/1.
 */
public interface Clock extends Destructible {

    void apply(Execution execution, Date date) throws NegativeDelayException;

    void apply(Execution execution, long time) throws NegativeDelayException;

    void apply(Execution execution, long delay, TimeUnit unit) throws NegativeDelayException;

}
