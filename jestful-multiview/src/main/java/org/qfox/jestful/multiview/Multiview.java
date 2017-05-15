package org.qfox.jestful.multiview;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.BeanConfigException;

import java.util.Map;

/**
 * Created by yangchangpei on 17/5/15.
 */
public class Multiview implements Plugin, Initialable, Destroyable {

    @Override
    public void initialize(BeanContainer beanContainer) {

    }

    @Override
    public void config(Map<String, String> arguments) throws BeanConfigException {

    }

    @Override
    public Object react(Action action) throws Exception {
        return null;
    }

    @Override
    public void destroy() {

    }
}
