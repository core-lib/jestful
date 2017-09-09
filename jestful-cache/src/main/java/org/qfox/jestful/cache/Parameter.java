package org.qfox.jestful.cache;

import org.qfox.jestful.cache.evaluation.Evaluator;
import org.qfox.jestful.cache.exception.IllegalTypeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yangchangpei on 17/9/9.
 */
public class Parameter implements AnnotatedElement {
    private final Method method;
    private final int index;
    private final Object value;
    private final Map<Class<? extends Annotation>, Annotation> annotations = new LinkedHashMap<Class<? extends Annotation>, Annotation>();

    public Parameter(Method method, int index, Object value) {
        this.method = method;
        this.index = index;
        this.value = value;
        Annotation[] annotations = method.getParameterAnnotations()[index];
        for (Annotation annotation : annotations) this.annotations.put(annotation.annotationType(), annotation);
    }

    public boolean isIgnored() {
        return isAnnotationPresent(Ignored.class);
    }

    public boolean isRefresh() {
        return isAnnotationPresent(Refresh.class);
    }

    public boolean toFlag() throws IllegalTypeException {
        Refresh duration = getAnnotation(Refresh.class);
        if (duration != null) {
            if (value instanceof Boolean) return Boolean.class.cast(value);
            else throw new IllegalTypeException("refresh flag argument should be instance of bool type");
        } else {
            throw new IllegalStateException();
        }
    }

    public boolean isDuration() {
        return isAnnotationPresent(Duration.class);
    }

    public Period toPeriod() throws IllegalTypeException {
        Duration duration = getAnnotation(Duration.class);
        if (duration != null) {
            if (value instanceof Long) return new Period(duration.unit(), (Long) value);
            else throw new IllegalTypeException("duration argument should be instance of long type");
        } else {
            throw new IllegalStateException();
        }
    }

    public boolean evaluate(Evaluator evaluator) {
        Key key = getAnnotation(Key.class);
        if (key != null && key.condition().length() > 0) {
            String condition = key.condition();
            return evaluator.evaluate(condition, value, Boolean.class);
        } else {
            return true;
        }
    }

    public String convert(Conversion conversion) {
        if (value instanceof Caching) return Caching.class.cast(value).toCacheKey();
        Key key = getAnnotation(Key.class);
        if (key != null && key.converter() != Converter.DEFAULT.class) {
            Converter converter = conversion.construct(key.converter());
            return converter.convert(value, conversion);
        } else {
            return conversion.convert(value);
        }
    }

    public String name() {
        Key key = getAnnotation(Key.class);
        return key != null && key.value().trim().length() > 0 ? key.value().trim() : String.valueOf(index);
    }

    public Method getMethod() {
        return method;
    }

    public int getIndex() {
        return index;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return annotationClass.cast(annotations.get(annotationClass));
    }

    @Override
    public Annotation[] getAnnotations() {
        return annotations.values().toArray(new Annotation[0]);
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return getAnnotations();
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return annotations.containsKey(annotationClass);
    }
}
