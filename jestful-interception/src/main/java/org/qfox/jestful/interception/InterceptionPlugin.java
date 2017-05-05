package org.qfox.jestful.interception;

import org.qfox.jestful.commons.tree.Node;
import org.qfox.jestful.commons.tree.PathExpression;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.BeanConfigException;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Payne on 2017/5/5.
 */
public class InterceptionPlugin implements Plugin, Initialable, Destroyable {
    private final Node<PathExpression, Interceptors> tree = new Node<PathExpression, Interceptors>(new PathExpression(null));

    @Override
    public void config(Map<String, String> arguments) throws BeanConfigException {

    }

    @Override
    public Object react(Action action) throws Exception {

        return null;
    }

    @Override
    public void initialize(BeanContainer beanContainer) {
        Collection<Interceptor> interceptors = beanContainer.find(Interceptor.class).values();
        for (Interceptor interceptor : interceptors) tree.merge(new NodeInterceptor(interceptor).toNode());
    }

    @Override
    public void destroy() {

    }

}
