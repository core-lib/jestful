package org.qfox.jestful.server.resolver;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;

/**
 * Created by yangchangpei on 17/5/2.
 */
public interface Resolver {

    boolean supports(Action action, Parameter parameter);

    void resolve(Action action, Parameter parameter) throws Exception;

}
