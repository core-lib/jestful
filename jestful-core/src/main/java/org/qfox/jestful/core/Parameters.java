package org.qfox.jestful.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
public class Parameters implements List<Parameter> {
	private final List<Parameter> parameters;

	public Parameters(Collection<Parameter> parameters) {
		super();
		this.parameters = new ArrayList<Parameter>(parameters);
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

	public int count(Position position) {
		int total = 0;
		for (Parameter parameter : parameters) {
			if (parameter.getPosition() == position) {
				total++;
			}
		}
		return total;
	}

	public List<Parameter> all(Class<?> klass) {
		List<Parameter> all = new ArrayList<Parameter>();
		for (Parameter parameter : parameters) {
			if (klass.isAssignableFrom(parameter.getKlass())) {
				all.add(parameter);
			}
		}
		return all;
	}
	
	public int count(Class<?> klass) {
		int total = 0;
		for (Parameter parameter : parameters) {
			if (klass.isAssignableFrom(parameter.getKlass())) {
				total++;
			}
		}
		return total;
	}

	public Parameter first(Class<?> klass) {
		Parameter first = null;
		for (Parameter parameter : parameters) {
			if (klass.isAssignableFrom(parameter.getKlass())) {
				first = parameter;
				break;
			}
		}
		return first;
	}

	public Parameter last(Class<?> klass) {
		Parameter last = null;
		for (Parameter parameter : parameters) {
			if (klass.isAssignableFrom(parameter.getKlass())) {
				last = parameter;
				continue;
			}
		}
		return last;
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

	public int size() {
		return parameters.size();
	}

	public boolean isEmpty() {
		return parameters.isEmpty();
	}

	public boolean contains(Object o) {
		return parameters.contains(o);
	}

	public Iterator<Parameter> iterator() {
		return parameters.iterator();
	}

	public Object[] toArray() {
		return parameters.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return parameters.toArray(a);
	}

	public boolean add(Parameter e) {
		return parameters.add(e);
	}

	public boolean remove(Object o) {
		return parameters.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return parameters.containsAll(c);
	}

	public boolean addAll(Collection<? extends Parameter> c) {
		return parameters.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends Parameter> c) {
		return parameters.addAll(index, c);
	}

	public boolean removeAll(Collection<?> c) {
		return parameters.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return parameters.retainAll(c);
	}

	public void clear() {
		parameters.clear();
	}

	public Parameter get(int index) {
		return parameters.get(index);
	}

	public Parameter set(int index, Parameter element) {
		return parameters.set(index, element);
	}

	public void add(int index, Parameter element) {
		parameters.add(index, element);
	}

	public Parameter remove(int index) {
		return parameters.remove(index);
	}

	public int indexOf(Object o) {
		return parameters.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return parameters.lastIndexOf(o);
	}

	public ListIterator<Parameter> listIterator() {
		return parameters.listIterator();
	}

	public ListIterator<Parameter> listIterator(int index) {
		return parameters.listIterator(index);
	}

	public List<Parameter> subList(int fromIndex, int toIndex) {
		return parameters.subList(fromIndex, toIndex);
	}

}
