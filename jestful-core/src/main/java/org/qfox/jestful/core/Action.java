package org.qfox.jestful.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.qfox.jestful.core.exception.DuplicateExecuteException;

/**
 * <p>
 * Description: A request-response job is an Action
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年1月15日 下午8:50:44
 *
 * @since 1.0.0
 */
public class Action {
	private final List<Actor> actors;
	private int index = 0;

	private Class<?> interfase;
	private Method method;
	private Object[] arguments;

	private Map<Object, Object> extra = new LinkedHashMap<Object, Object>();

	public Action(Collection<Actor> actors) {
		super();
		this.actors = new ArrayList<Actor>(actors);
	}

	/**
	 * pass to the next actor to continue this action
	 * 
	 * @return action result
	 * @throws Exception
	 *             all type exception may be thrown
	 */
	public Object execute() throws Exception {
		if (index == actors.size()) {
			throw new DuplicateExecuteException(this);
		}
		return actors.get(index++).react(this);
	}

	/**
	 * insert the actors to the nearest position
	 * 
	 * @param intruders
	 *            the first actors will execute this action
	 */
	public void intrude(Actor... intruders) {
		actors.addAll(index, Arrays.asList(intruders));
	}

	/**
	 * insert the actors to the nearest position
	 * 
	 * @param intruders
	 *            the first actors will execute this action
	 */
	public void intrude(List<Actor> intruders) {
		actors.addAll(index, intruders);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Class<?> getInterfase() {
		return interfase;
	}

	public void setInterfase(Class<?> interfase) {
		this.interfase = interfase;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public Map<Object, Object> getExtra() {
		return extra;
	}

	public void setExtra(Map<Object, Object> extra) {
		this.extra = extra;
	}

}
