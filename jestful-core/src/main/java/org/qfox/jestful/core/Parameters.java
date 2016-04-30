package org.qfox.jestful.core;

import java.util.ArrayList;
import java.util.Arrays;
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
	private final List<Parameter> elements;

	public Parameters(Parameter[] elements) {
		super();
		this.elements = Arrays.asList(elements);
	}

	public int size() {
		return elements.size();
	}

	public Parameter get(int index) {
		return elements.get(index);
	}

	public List<Parameter> all(Position position) {
		List<Parameter> parameters = new ArrayList<Parameter>();
		for (Parameter element : elements) {
			if (element.getPosition() == position) {
				parameters.add(element);
			}
		}
		return parameters;
	}

	public Object[] arguments() {
		Object[] arguments = new Object[size()];
		for (int i = 0; i < size(); i++) {
			arguments[i] = elements.get(i).getValue();
		}
		return arguments;
	}

	public void arguments(Object[] arguments) {
		if (arguments.length != size()) {
			throw new IllegalArgumentException("Unexpected array length " + arguments.length);
		}
		for (int i = 0; i < size(); i++) {
			elements.get(i).setValue(arguments[i]);
		}
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public Iterator<Parameter> iterator() {
		return elements.iterator();
	}

}
