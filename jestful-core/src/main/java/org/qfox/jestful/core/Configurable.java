package org.qfox.jestful.core;

import org.qfox.jestful.core.exception.BeanConfigException;

import java.util.Map;

/**
 * Created by Payne on 2017/4/27.
 */
public interface Configurable {

    void config(Map<String, String> arguments) throws BeanConfigException;

}
