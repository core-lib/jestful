package org.qfox.jestful.interception;

import org.qfox.jestful.commons.tree.PathExpression;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Configurable;
import org.qfox.jestful.core.Destroyable;
import org.qfox.jestful.core.annotation.Command;
import org.qfox.jestful.core.exception.BeanConfigException;
import org.qfox.jestful.interception.annotation.Commands;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Payne on 2017/5/5.
 */
class Listener implements Interceptor, Configurable, Destroyable, Sequential {
    private final List<PathExpression> expressions = new ArrayList<PathExpression>();
    private final Interceptor interceptor;
    private int sequence = 0;

    Listener(Interceptor interceptor) {
        try {
            Annotation[] annotations = interceptor.getClass().getMethod("intercept", Invocation.class).getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().isAnnotationPresent(Command.class)) {
                    Command command = annotation.annotationType().getAnnotation(Command.class);
                    String value = annotation.annotationType().getMethod("value").invoke(annotation).toString();
                    String regex = ("/" + value).replaceAll("/+", "/").replaceAll("/+$", "");
                    PathExpression expression = new PathExpression(regex, command.name(), null);
                    this.expressions.add(expression);
                }

                if (annotation.annotationType().isAnnotationPresent(Commands.class)) {
                    Annotation[] commands = (Annotation[]) annotation.annotationType().getMethod("value").invoke(annotation);
                    for (Annotation command : commands) {
                        String value = (String) command.annotationType().getMethod("value").invoke(command);
                        String method = command.annotationType().getAnnotation(Command.class).name();
                        String regex = ("/" + value).replaceAll("/+", "/").replaceAll("/+$", "");
                        PathExpression expression = new PathExpression(regex, method, null);
                        this.expressions.add(expression);
                    }
                }
            }
            this.interceptor = interceptor;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    boolean matches(Action action) {
        String URI = action.getURI();
        String method = action.getRestful().getMethod();
        for (PathExpression e : expressions) if (e.getMethod().equalsIgnoreCase(method) && e.match(URI)) return true;
        return false;
    }

    @Override
    public Object intercept(Invocation invocation) throws Exception {
        return interceptor.intercept(invocation);
    }

    @Override
    public void config(Map<String, String> arguments) throws BeanConfigException {
        if (interceptor instanceof Configurable) ((Configurable) interceptor).config(arguments);
    }

    @Override
    public void destroy() {
        if (interceptor instanceof Destroyable) ((Destroyable) interceptor).destroy();
    }

    @Override
    public int getSequence() {
        return sequence;
    }
}
