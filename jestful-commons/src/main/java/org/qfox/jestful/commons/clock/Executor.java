package org.qfox.jestful.commons.clock;

import org.qfox.jestful.commons.Destructible;

/**
 * Created by Payne on 2017/12/1.
 */
public interface Executor extends Destructible {

    void execute(Execution execution);

}
