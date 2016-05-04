package org.qfox.jestful.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
 * @date 2016年4月30日 下午4:50:49
 *
 * @since 1.0.0
 */
public class Parameters implements Iterable<Parameter> {
	private final List<Parameter> parameters;

	public Parameters(Collection<Parameter> parameters) {
		super();
		this.parameters = new ArrayList<Parameter>(parameters);
	}

	public int size() {
		return parameters.size();
	}

	public Parameter get(int index) {
		return parameters.get(index);
	}

	public List<Parameter> all(Position position) {
		List<Parameter> all = new ArrayList<Parameter>();
		for (Parameter parameter : parameters) {
			if (parameter.getPosition() == position) {
				all.add(parameter);
			}
		}
		return all;
	}

	public Object[] arguments() {
		Object[] arguments = new Object[size()];
		for (int i = 0; i < size(); i++) {
			arguments[i] = parameters.get(i).getValue();
		}
		return arguments;
	}

	public void arguments(Object[] arguments) {
		if (arguments.length != size()) {
			throw new IllegalArgumentException("Unexpected array length " + arguments.length);
		}
		for (int i = 0; i < size(); i++) {
			parameters.get(i).setValue(arguments[i]);
		}
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public Iterator<Parameter> iterator() {
		return parameters.iterator();
	}

}
