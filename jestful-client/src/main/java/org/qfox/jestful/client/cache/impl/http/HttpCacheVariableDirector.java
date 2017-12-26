package org.qfox.jestful.client.cache.impl.http;

import org.qfox.jestful.client.cache.impl.http.annotation.CacheDirective;
import org.qfox.jestful.client.exception.NotAllowedTypeException;
import org.qfox.jestful.commons.Primaries;
import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.core.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class HttpCacheVariableDirector implements Actor, HttpCacheConstants {

    @Override
    public Object react(Action action) throws Exception {
        final Parameters parameters = action.getParameters();
        final List<Directive> directives = new ArrayList<Directive>();
        for (Parameter parameter : parameters) {
            final Annotation[] annotations = parameter.getAnnotationsWith(CacheDirective.class);
            if (annotations != null && annotations.length > 0) directives.add(new Directive(parameter, annotations));
        }
        if (directives.isEmpty()) return action.execute();

        final Request request = action.getRequest();
        final String cc = request.getRequestHeader(CACHE_CONTROL);
        final HttpCacheControl hcc = HttpCacheControl.valueOf(cc != null ? cc : "");

        for (final Directive directive : directives) {
            final Map<String, String> map = directive.toMap();
            for (final Map.Entry<String, String> entry : map.entrySet()) {
                final String key = entry.getKey();
                final String value = entry.getValue();
                if (value == null) hcc.remove(key);
                else hcc.put(key, value);
            }
        }

        request.setRequestHeader(CACHE_CONTROL, hcc.toString());

        return action.execute();
    }

    private static class Directive {
        private final Parameter parameter;
        private final Annotation[] annotations;

        private Directive(Parameter parameter, Annotation[] annotations) throws NotAllowedTypeException {
            if (parameter == null) throw new NullPointerException("parameter == null");
            if (annotations == null) throw new NullPointerException("annotations == null");
            if (annotations.length == 0) throw new IllegalArgumentException("annotations is empty");

            final Class<?> klass = parameter.getKlass();
            final Class<?> type = Primaries.isPrimaryType(klass) ? Primaries.getWrapperType(klass) : klass;
            flag:
            for (Annotation annotation : annotations) {
                final CacheDirective cd = annotation.annotationType().getAnnotation(CacheDirective.class);
                if (cd == null) throw new IllegalArgumentException(annotation + " is not a cache directive annotation");
                final Class<?>[] classes = cd.type();
                for (Class<?> c : classes) {
                    if (Primaries.isPrimaryType(c)) c = Primaries.getWrapperType(c);
                    if (c.isAssignableFrom(type)) continue flag;
                }
                throw new NotAllowedTypeException(annotation + " should annotate a parameter which type in " + Arrays.toString(classes) + " but got " + type);
            }

            this.parameter = parameter;
            this.annotations = annotations;
        }

        public Map<String, String> toMap() {
            Map<String, String> map = new LinkedHashMap<String, String>(annotations.length);
            for (final Annotation annotation : annotations) {
                final CacheDirective cd = annotation.annotationType().getAnnotation(CacheDirective.class);
                String name = cd.name();
                if (StringKit.isBlank(name)) name = value(annotation);
                if (StringKit.isBlank(name)) continue;
                HttpCacheDirectiveResolver resolver = newInstance(cd.resolver());
                Object value = parameter.getValue();
                map.put(name, resolver.resolve(annotation, value));
            }
            return map;
        }

        private String value(Annotation annotation) {
            try {
                Method method = annotation.annotationType().getMethod("value");
                return (String) method.invoke(annotation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private HttpCacheDirectiveResolver<?> newInstance(Class<? extends HttpCacheDirectiveResolver<?>> resolver) {
            try {
                return resolver.getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Parameter getParameter() {
            return parameter;
        }

        public Annotation[] getAnnotations() {
            return annotations;
        }
    }

}
