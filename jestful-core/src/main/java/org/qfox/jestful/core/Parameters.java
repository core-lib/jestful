package org.qfox.jestful.core;

import org.qfox.jestful.core.exception.NoOnlyParameterException;
import org.qfox.jestful.core.exception.NoSuchParameterException;

import java.util.*;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月30日 下午4:50:49
 * @since 1.0.0
 */
public class Parameters implements List<Parameter> {
    private final List<Parameter> parameters;

    public Parameters(Collection<Parameter> parameters) {
        super();
        this.parameters = new ArrayList<Parameter>(parameters);
    }

    public List<Parameter> all(int pos) {
        return all(pos, false);
    }

    public List<Parameter> all(int pos, boolean strict) {
        List<Parameter> all = new ArrayList<Parameter>();
        for (Parameter parameter : parameters) if (strict ? parameter.getPosition() == pos : parameter.between(pos)) all.add(parameter);
        return all;
    }

    public int count(int pos) {
        return count(pos, false);
    }

    public int count(int pos, boolean strict) {
        int total = 0;
        for (Parameter parameter : parameters) if (strict ? parameter.getPosition() == pos : parameter.between(pos)) total++;
        return total;
    }

    public List<Parameter> all(Class<?> klass) {
        List<Parameter> all = new ArrayList<Parameter>();
        for (Parameter parameter : parameters) if (klass.isAssignableFrom(parameter.getKlass())) all.add(parameter);
        return all;
    }

    public int count(Class<?> klass) {
        int total = 0;
        for (Parameter parameter : parameters) if (klass.isAssignableFrom(parameter.getKlass())) total++;
        return total;
    }

    public Parameter unique(Class<?> klass) throws NoSuchParameterException, NoOnlyParameterException {
        List<Parameter> all = all(klass);
        if (all.isEmpty()) throw new NoSuchParameterException(this, klass);
        else if (all.size() > 1) throw new NoOnlyParameterException(this, klass);
        else return all.get(0);
    }

    public Parameter first(Class<?> klass) {
        for (Parameter parameter : parameters) if (klass.isAssignableFrom(parameter.getKlass())) return parameter;
        return null;
    }

    public Parameter last(Class<?> klass) {
        Parameter last = null;
        for (Parameter parameter : parameters) if (klass.isAssignableFrom(parameter.getKlass())) last = parameter;
        return last;
    }

    public Object[] values() {
        Object[] arguments = new Object[size()];
        for (int i = 0; i < size(); i++) arguments[i] = parameters.get(i).getValue();
        return arguments;
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
