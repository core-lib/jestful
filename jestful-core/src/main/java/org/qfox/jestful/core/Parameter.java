package org.qfox.jestful.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.qfox.jestful.core.annotation.Argument;
import org.qfox.jestful.core.annotation.Argument.Position;
import org.qfox.jestful.core.exception.IllegalConfigException;

/**
 * <p>
 * Description: 形参
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月7日 下午3:56:59
 *
 * @since 1.0.0
 */
public class Parameter extends Configuration implements Comparable<Parameter> {
	private final Mapping mapping;
	private final Object controller;
	private final Method method;
	private final Type type;
	private final int index;
	private final String name;
	private final Position position;
	private Object value;
	private int group;
	private String regex;

	public Parameter(Mapping mapping, Method method, int index) throws IllegalConfigException {
		super(method.getParameterAnnotations()[index]);
		try {
			this.mapping = mapping;
			this.controller = mapping.getController();
			this.method = method;
			this.type = method.getGenericParameterTypes()[index];
			this.index = index;
			Annotation annotation = getAnnotationWith(Argument.class);
			String name = annotation.annotationType().getMethod("value").invoke(annotation).toString();
			this.name = name.isEmpty() ? String.valueOf(index) : name;
			Argument argument = annotation.annotationType().getAnnotation(Argument.class);
			this.position = argument.position();
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

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public Position getPosition() {
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
