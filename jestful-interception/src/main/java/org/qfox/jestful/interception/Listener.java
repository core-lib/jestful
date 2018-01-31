package org.qfox.jestful.interception;

import org.qfox.jestful.commons.tree.PathExpression;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Configurable;
import org.qfox.jestful.core.Destroyable;
import org.qfox.jestful.core.annotation.Function;
import org.qfox.jestful.core.exception.BeanConfigException;
import org.qfox.jestful.interception.annotation.Functions;

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
                if (annotation.annotationType().isAnnotationPresent(Function.class)) {
                    Function function = annotation.annotationType().getAnnotation(Function.class);
                    String value = annotation.annotationType().getMethod("value").invoke(annotation).toString();
                    String regex = ("/" + value).replaceAll("/+", "/").replaceAll("/+$", "");
                    PathExpression expression = new PathExpression(regex, function.name(), null);
                    this.expressions.add(expression);
                }

                if (annotation.annotationType().isAnnotationPresent(Functions.class)) {
                    Annotation[] functions = (Annotation[]) annotation.annotationType().getMethod("value").invoke(annotation);
                    for (Annotation function : functions) {
                        String value = (String) function.annotationType().getMethod("value").invoke(function);
                        String method = function.annotationType().getAnnotation(Function.class).name();
                        String regex = ("/" + value).replaceAll("/+", "/").replaceAll("/+$", "");
                        PathExpression expression = new PathExpression(regex, method, null);
                        this.expressions.add(expression);
                    }
                }
            }
            this.interceptor = interceptor;
            this.sequence = interceptor.getClass().isAnnotationPresent(Sequence.class)
                    ? interceptor.getClass().getAnnotation(Sequence.class).value()
                    : interceptor instanceof Sequential
                    ? ((Sequential) interceptor).getSequence()
                    : 0;
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
