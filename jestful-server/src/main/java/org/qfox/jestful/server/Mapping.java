package org.qfox.jestful.server;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.qfox.jestful.core.annotation.Method;
import org.qfox.jestful.core.annotation.Path;
import org.qfox.jestful.server.exception.AlreadyValuedException;
import org.qfox.jestful.server.exception.DuplicateArgumentException;
import org.qfox.jestful.server.exception.DuplicateVariableException;
import org.qfox.jestful.server.exception.IllegalConfigException;
import org.qfox.jestful.server.exception.UndefinedVariableException;
import org.qfox.jestful.server.tree.Hierarchical;
import org.qfox.jestful.server.tree.Node;
import org.qfox.jestful.server.tree.PathExpression;

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
 * @date 2016年3月31日 上午11:32:21
 *
 * @since 1.0.0
 */
public class Mapping implements Hierarchical<PathExpression, Mapping>, Comparable<Mapping> {
	private static final Pattern PATTERN = Pattern.compile("\\{(?<name>[^{}]+?)(:(?<rule>[^{}]+?))?\\}");

	private final Operation operation;
	private final Method method;
	private final String definition;
	private final String expression;
	private final Set<Variable> variables = new TreeSet<Variable>();

	public Mapping(Operation operation, Method method, String definition) throws IllegalConfigException {
		super();
		this.operation = operation;
		this.method = method;
		this.definition = definition;
		String expression = definition;
		Matcher matcher = PATTERN.matcher(definition);
		while (matcher.find()) {
			String name = matcher.group("name");
			String rule = matcher.group("rule");
			rule = rule != null ? rule : ".*?";
			// 找到对应的方法参数
			Path path = null;
			int index = 0;
			for (int i = 0; i < operation.getConfigurer().getParameterAnnotations().length; i++) {
				for (Annotation annotation : operation.getConfigurer().getParameterAnnotations()[i]) {
					if (annotation instanceof Path && ((Path) annotation).value().equals(name)) {
						if (path != null) {
							throw new DuplicateArgumentException(operation.getResource().getController(), operation.getConfigurer(), Arrays.asList(index, i));
						} else {
							index = i;
							path = (Path) annotation;
						}
					}
				}
			}

			if (path == null) {
				if (name.matches("\\d+")) {
					index = Integer.valueOf(name) - 1;
					if (index < 0 || index >= operation.getConfigurer().getParameterAnnotations().length) {
						throw new UndefinedVariableException(operation.getResource().getController(), operation.getConfigurer(), index);
					}
				} else {
					throw new UndefinedVariableException(operation.getResource().getController(), operation.getConfigurer(), name);
				}
			}

			Variable variable = new Variable(name, index, matcher.group(), rule);
			if (variables.add(variable)) {
				expression = expression.replace(matcher.group(), rule);
			} else {
				for (Variable v : variables) {
					if (v.equals(variable)) {
						throw new DuplicateVariableException(operation.getResource().getController(), operation.getConfigurer(), variable, v);
					}
				}
			}
		}
		this.expression = expression;
	}

	public Node<PathExpression, Mapping> toNode() throws AlreadyValuedException {
		String[] hierarchies = expression.split("\\/+");
		Iterator<String> iterator = Arrays.asList(hierarchies).iterator();
		Node<PathExpression, Mapping> result = null;
		Node<PathExpression, Mapping> parent = null;
		while (iterator.hasNext()) {
			String hierarchy = iterator.next();
			if (hierarchy.isEmpty()) {
				continue;
			}
			Node<PathExpression, Mapping> branch = new Node<PathExpression, Mapping>(new PathExpression(hierarchy, iterator.hasNext() ? null : method.name()));
			branch.setValue(iterator.hasNext() ? null : this);
			if (result == null) {
				result = branch;
				parent = branch;
			} else {
				parent.getBranches().add(branch);
				parent = branch;
			}
		}
		if (result == null) {
			result = new Node<PathExpression, Mapping>(new PathExpression(null, method.name()));
			result.setValue(this);
		}
		return result;
	}

	public int compareTo(Mapping o) {
		return expression.compareTo(o.expression) != 0 ? expression.compareTo(o.expression) : method.name().compareTo(o.method.name());
	}

	public Operation getOperation() {
		return operation;
	}

	public Method getMethod() {
		return method;
	}

	public String getDefinition() {
		return definition;
	}

	public String getExpression() {
		return expression;
	}

	@Override
	public String toString() {
		return method.name() + " : " + operation.toString();
	}

	public String toLogString() {
		return method.name() + " : " + definition + " " + operation.toString();
	}

}
