package org.qfox.jestful.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.qfox.jestful.core.annotation.Move;
import org.qfox.jestful.core.exception.AmbiguousResultException;
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
	private Object value;
	private final Movement movement;
	private final String expression;
	private final boolean invoke;

	public Result(Mapping mapping, Method method) throws IllegalConfigException {
		super(method.getAnnotations());
		try {
			this.mapping = mapping;
			this.controller = mapping.getController();
			this.method = method;
			this.type = method.getGenericReturnType();
			Annotation[] moves = getAnnotationsWith(Move.class);
			if (moves.length == 1) {
				Annotation move = getAnnotationWith(Move.class);
				this.movement = move.annotationType().getAnnotation(Move.class).value();
				this.expression = (String) move.annotationType().getMethod("value").invoke(move);
				this.invoke = (Boolean) move.annotationType().getMethod("invoke").invoke(move);
			} else if (moves.length == 0) {
				this.movement = null;
				this.expression = null;
				this.invoke = true;
			} else {
				throw new AmbiguousResultException("Ambiguous result of method " + method + " has more than one move kind annotations " + Arrays.toString(moves), controller, method, this);
			}
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

	public Movement getMovement() {
		return movement;
	}

	public String getExpression() {
		return expression;
	}

	public boolean isInvoke() {
		return invoke;
	}

}
