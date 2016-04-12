package org.qfox.jestful.core;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.qfox.jestful.core.annotation.Return;
import org.qfox.jestful.core.exception.IllegalConfigException;

/**
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月12日 下午7:31:24
 *
 * @since 1.0.0
 */
public class Result extends Configuration {
	private final Mapping mapping;
	private final Object controller;
	private final Method method;
	private final Type type;
	private final String name;
	private final Location location;
	private Object value;

	public Result(Mapping mapping, Method method) throws IllegalConfigException {
		super(method.getAnnotations());
		try {
			this.mapping = mapping;
			this.controller = mapping.getController();
			this.method = method;
			this.type = method.getGenericReturnType();
			this.name = isAnnotationPresent(Return.class) ? getAnnotation(Return.class).name() : null;
			this.location = isAnnotationPresent(Return.class) ? getAnnotation(Return.class).location() : Location.BODY;
		} catch (Exception e) {
			throw new IllegalConfigException(e, mapping.getController(), method);
		}
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
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

	public String getName() {
		return name;
	}

	public Location getLocation() {
		return location;
	}

}
