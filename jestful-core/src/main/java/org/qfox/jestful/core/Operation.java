package org.qfox.jestful.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.commons.tree.Hierarchical;
import org.qfox.jestful.commons.tree.Node;
import org.qfox.jestful.commons.tree.PathExpression;
import org.qfox.jestful.core.annotation.Command;
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
 * @date 2016年3月31日 上午11:27:39
 *
 * @since 1.0.0
 */
public class Operation extends Annotated implements Hierarchical<PathExpression, Mapping> {
	private final Resource resource;
	private final Object controller;
	private final Method method;
	private final Method configuration;
	private final Map<String, Mapping> mappings = new CaseInsensitiveMap<String, Mapping>();

	public Operation(Resource resource, Method method, Method configuration) throws IllegalConfigException {
		super(configuration.getAnnotations());
		this.resource = resource;
		this.controller = resource.getController();
		this.method = method;
		this.configuration = configuration;
		for (Annotation annotation : annotations) {
			Command command = annotation.annotationType().getAnnotation(Command.class);
			if (command == null) {
				continue;
			}
			Mapping mapping = new Mapping(this, annotations, command);
			this.mappings.put(command.name(), mapping);
		}
	}

	public Node<PathExpression, Mapping> toNode() {
		Node<PathExpression, Mapping> result = new Node<PathExpression, Mapping>(new PathExpression());
		for (Mapping mapping : mappings.values()) {
			Node<PathExpression, Mapping> node = mapping.toNode();
			Node<PathExpression, Mapping> parent = new Node<PathExpression, Mapping>(new PathExpression());
			parent.getBranches().add(node);
			result.merge(parent);
		}
		return result;
	}

	public Resource getResource() {
		return resource;
	}

	public Object getController() {
		return controller;
	}

	public Method getMethod() {
		return method;
	}

	public Method getConfiguration() {
		return configuration;
	}

	public Map<String, Mapping> getMappings() {
		return mappings;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
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
		Operation other = (Operation) obj;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return method.toGenericString();
	}

}
