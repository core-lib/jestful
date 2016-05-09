package org.qfox.jestful.core;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

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
	private final Class<?> klass;
	private final Body body;
	private Object value;
	private boolean rendered = false;

	public Result(Mapping mapping, Method method) throws IllegalConfigException {
		super(method.getAnnotations());
		try {
			this.mapping = mapping;
			this.controller = mapping.getController();
			this.method = method;
			this.type = method.getGenericReturnType();
			this.klass = method.getReturnType();
			this.body = new Body(type);
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

	public boolean isRendered() {
		return rendered;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
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

	public Body getBody() {
		return body;
	}

}
