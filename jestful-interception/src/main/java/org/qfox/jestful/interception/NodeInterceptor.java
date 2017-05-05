package org.qfox.jestful.interception;

import org.qfox.jestful.commons.tree.Hierarchical;
import org.qfox.jestful.commons.tree.Node;
import org.qfox.jestful.commons.tree.PathExpression;
import org.qfox.jestful.core.annotation.Command;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Payne on 2017/5/5.
 */
class NodeInterceptor implements Interceptor, Comparable<NodeInterceptor>, Hierarchical<PathExpression, Interceptors> {
    private final List<PathExpression> expressions = new ArrayList<PathExpression>();
    private final Interceptor interceptor;

    NodeInterceptor(Interceptor interceptor) {
        try {
            Method method = interceptor.getClass().getMethod("intercept", Invocation.class);
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (!annotation.annotationType().isAnnotationPresent(Command.class)) {
                    continue;
                }
                Command command = annotation.annotationType().getAnnotation(Command.class);
                String value = annotation.annotationType().getMethod("value").invoke(annotation).toString();
                String regex = ("/" + value).replaceAll("/+", "/").replaceAll("/+$", "");
                PathExpression expression = new PathExpression(regex, command.name(), null);
                this.expressions.add(expression);
            }
            this.interceptor = interceptor;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Node<PathExpression, Interceptors> toNode() {
        Node<PathExpression, Interceptors> tree = new Node<PathExpression, Interceptors>(new PathExpression(null));
        for (PathExpression expression : expressions) {
            Node<PathExpression, Interceptors> node = new Node<PathExpression, Interceptors>(expression);
            node.setValue(new Interceptors(interceptor));
            tree.merge(node);
        }
        return tree;
    }

    @Override
    public Object intercept(Invocation invocation) throws Exception {
        return interceptor.intercept(invocation);
    }

    @Override
    public int compareTo(NodeInterceptor o) {
        return 0;
    }

}
