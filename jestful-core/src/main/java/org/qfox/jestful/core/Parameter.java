package org.qfox.jestful.core;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.qfox.jestful.core.annotation.Name;
import org.qfox.jestful.core.annotation.Place;

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
public class Parameter extends Annotated implements Comparable<Parameter> {
	private final Method method;
	private final Type type;
	private final int index;
	private final String name;
	private final String place;
	private Object value;
	private int group;
	private String regex;

	public Parameter(Method method, int index) {
		super(method.getParameterAnnotations()[index]);
		this.method = method;
		this.type = method.getGenericParameterTypes()[index];
		this.index = index;
		this.name = isAnnotationPresent(Name.class) ? getAnnotation(Name.class).value() : String.valueOf(index);
		this.place = isAnnotationPresent(Place.class) ? getAnnotation(Place.class).value() : null;
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

	public String getPlace() {
		return place;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((place == null) ? 0 : place.hashCode());
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
		if (place != other.place)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return type.toString();
	}

}
