package org.qfox.jestful.core;

import org.qfox.jestful.commons.MethodKit;
import org.qfox.jestful.core.annotation.Variable;
import org.qfox.jestful.core.exception.AmbiguousParameterException;
import org.qfox.jestful.core.exception.IllegalConfigException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Description: 形参
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月7日 下午3:56:59
 * @since 1.0.0
 */
public class Parameter extends Configuration implements Comparable<Parameter> {
    private final Mapping mapping;
    private final Object controller;
    private final Method method;
    private final Type type;
    private final Class<?> klass;
    private final int index;
    private final String name;
    private final int position;
    private final boolean coding;
    private final boolean encoded;
    private final boolean decoded;
    private Object value;
    private int group;
    private String regex;
    private boolean resolved;

    public Parameter(Mapping mapping, Method method, int index) throws IllegalConfigException {
        super(method.getParameterAnnotations()[index]);
        try {
            this.mapping = mapping;
            this.controller = mapping.getController();
            this.method = method;
            this.type = method.getGenericParameterTypes()[index];
            this.klass = method.getParameterTypes()[index];
            this.index = index;
            Annotation[] variables = getAnnotationsWith(Variable.class);
            if (variables.length == 1) {
                Annotation annotation = getAnnotationWith(Variable.class);
                String name = annotation.annotationType().getMethod("value").invoke(annotation).toString().trim();
                if (name.isEmpty()) {
                    List<String> names = MethodKit.parameters(method);
                    if (names != null && names.size() > index) name = names.get(index);
                    else throw new IllegalConfigException("unknown parameter name of index " + index + " in method " + method, mapping.getController(), method);
                }
                this.name = name;
                Variable variable = annotation.annotationType().getAnnotation(Variable.class);
                this.position = variable.position();
                this.coding = variable.coding();
                this.encoded = variable.coding() ? (Boolean) annotation.annotationType().getMethod("encoded").invoke(annotation) : false;
                this.decoded = variable.coding() ? (Boolean) annotation.annotationType().getMethod("decoded").invoke(annotation) : false;
            } else if (variables.length == 0) {
                this.name = String.valueOf(index);
                this.position = Position.UNKNOWN;
                this.coding = false;
                this.encoded = false;
                this.decoded = false;
            } else {
                throw new AmbiguousParameterException("Ambiguous parameter at index " + index + " in " + method + " which has more than one variable kind annotations " + Arrays.toString(variables), controller, method, this);
            }
        } catch (Exception e) {
            throw new IllegalConfigException(e, mapping.getController(), method);
        }
    }

    public int compareTo(Parameter o) {
        return index > o.index ? 1 : index < o.index ? -1 : 0;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public boolean isCoding() {
        return coding;
    }

    public boolean isEncoded() {
        return encoded;
    }

    public boolean isDecoded() {
        return decoded;
    }

    public Mapping getMapping() {
        return mapping;
    }

    public Object getController() {
        return controller;
    }

    public Method getMethod() {
        return method;
    }

    public Type getType() {
        return type;
    }

    public Class<?> getKlass() {
        return klass;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Parameter other = (Parameter) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return type.toString();
    }

}
