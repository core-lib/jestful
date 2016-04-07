package org.qfox.jestful.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

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
public class Parameter {
	private final Method method;
	private final Type type;
	private final int index;
	private final String name;
	private final Source source;
	private Object value;
	private final Map<Class<? extends Annotation>, Annotation> annotations = new LinkedHashMap<Class<? extends Annotation>, Annotation>();

	public Parameter(Method method, Type type, int index, String name, Source source) {
		super();
		this.method = method;
		this.type = type;
		this.index = index;
		this.name = name;
		this.source = source;
		for (Annotation annotation : method.getParameterAnnotations()[index]) {
			annotations.put(annotation.annotationType(), annotation);
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

	public Source getSource() {
		return source;
	}

	public Map<Class<? extends Annotation>, Annotation> getAnnotations() {
		return annotations;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		if (source != other.source)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return type.toString();
	}

}
