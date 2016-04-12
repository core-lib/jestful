package org.qfox.jestful.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.qfox.jestful.commons.tree.Hierarchical;
import org.qfox.jestful.commons.tree.Node;
import org.qfox.jestful.commons.tree.PathExpression;
import org.qfox.jestful.core.annotation.Command;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.exception.IllegalConfigException;

/**
 * <p>
 * Description: 资源
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年3月31日 上午10:09:23
 *
 * @since 1.0.0
 */
public class Resource extends Configuration implements Hierarchical<PathExpression, Mapping> {
	private final Object controller;
	private final String expression;
	private final Set<Mapping> mappings = new HashSet<Mapping>();

	public Resource(Object controller) throws IllegalConfigException {
		super(controller.getClass().getAnnotations());
		this.controller = controller;
		Jestful jestful = this.getAnnotation(Jestful.class);
		if (jestful == null) {
			throw new IllegalConfigException(controller.getClass() + " is not a resource controller because it did not annatated @" + Jestful.class.getSimpleName(), controller);
		}
		this.expression = ("/" + jestful.value() + "/").replaceAll("\\/+", "/");
		Method[] methods = controller.getClass().getMethods();
		for (Method method : methods) {
			if (method.isSynthetic()) {
				continue;
			}
			Method configuration = null;
			if ((configuration = getRestfulMethodFromClasses(method, controller.getClass())) != null) {
				this.mappings.add(new Mapping(this, controller, method, configuration));
				continue;
			}
			if ((configuration = getRestfulMethodFromInterfaces(method, controller.getClass())) != null) {
				this.mappings.add(new Mapping(this, controller, method, configuration));
				continue;
			}
		}
	}

	private static Method getRestfulMethodFromClasses(Method method, Class<?> clazz) {
		Class<?> superclass = clazz;

		while (superclass != null) {
			Method[] methods = superclass.getDeclaredMethods();
			flag: for (Method m : methods) {
				if (m.getName().equals(method.getName()) && m.getGenericParameterTypes().length == method.getGenericParameterTypes().length) {
					for (int i = 0; i < m.getGenericParameterTypes().length; i++) {
						Type actual = m.getGenericParameterTypes()[i];
						Type expect = method.getGenericParameterTypes()[i];
						if (actual.equals(expect) == false && actual instanceof TypeVariable<?> == false) {
							continue flag;
						}
					}
					// 在父类中找到了对应的被重写的方法, 判断是否有Command的注解
					for (Annotation annotation : m.getAnnotations()) {
						if (annotation.annotationType().isAnnotationPresent(Command.class)) {
							return m;
						}
					}
				}
			}
			superclass = superclass.getSuperclass();
		}

		return null;
	}

	private static Method getRestfulMethodFromInterfaces(Method method, Class<?> clazz) {
		Class<?> superclass = clazz;

		while (superclass != null) {
			for (Class<?> interfase : superclass.getInterfaces()) {
				Method[] methods = interfase.getDeclaredMethods();
				flag: for (Method m : methods) {
					if (m.getName().equals(method.getName()) && m.getGenericParameterTypes().length == method.getGenericParameterTypes().length) {
						for (int i = 0; i < m.getGenericParameterTypes().length; i++) {
							Type actual = m.getGenericParameterTypes()[i];
							Type expect = method.getGenericParameterTypes()[i];
							if (actual.equals(expect) == false && actual instanceof TypeVariable<?> == false) {
								continue flag;
							}
						}
						// 在接口中找到了对应的被实现的方法, 判断是否有restful的注解
						for (Annotation annotation : m.getAnnotations()) {
							if (annotation.annotationType().isAnnotationPresent(Command.class)) {
								return m;
							}
						}
						m = getRestfulMethodFromInterfaces(method, interfase);
						if (m != null) {
							return m;
						}
					}
				}
			}
			superclass = superclass.getSuperclass();
		}

		return null;
	}

	public Node<PathExpression, Mapping> toNode() {
		String[] hierarchies = expression.split("\\/+");
		Iterator<String> iterator = Arrays.asList(hierarchies).iterator();
		Node<PathExpression, Mapping> result = null;
		Node<PathExpression, Mapping> parent = null;
		while (iterator.hasNext()) {
			String hierarchy = iterator.next();
			if (hierarchy.trim().isEmpty()) {
				continue;
			}
			Node<PathExpression, Mapping> child = new Node<PathExpression, Mapping>(new PathExpression(hierarchy));
			if (result == null) {
				result = child;
				parent = child;
			} else {
				parent.getBranches().add(child);
				parent = child;
			}
		}
		if (result == null) {
			result = parent = new Node<PathExpression, Mapping>(new PathExpression());
		}
		for (Mapping mapping : mappings) {
			Node<PathExpression, Mapping> node = mapping.toNode();
			parent.merge(node);
		}
		return result;
	}

	public Object getController() {
		return controller;
	}

	public String getPath() {
		return expression;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expression == null) ? 0 : expression.hashCode());
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
		Resource other = (Resource) obj;
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "{" + expression + " : " + controller + "}";
	}

}
