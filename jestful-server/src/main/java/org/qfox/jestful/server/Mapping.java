package org.qfox.jestful.server;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.qfox.jestful.core.annotation.Command;
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
	private final Command command;
	private final String definition;
	private final String expression;
	private final Set<Variable> variables = new TreeSet<Variable>();

	public Mapping(Operation operation, Command command, String definition) throws IllegalConfigException {
		super();
		this.operation = operation;
		this.command = command;
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
			for (int i = 0; i < operation.getConfiguration().getParameterAnnotations().length; i++) {
				for (Annotation annotation : operation.getConfiguration().getParameterAnnotations()[i]) {
					if (annotation instanceof Path && ((Path) annotation).value().equals(name)) {
						if (path != null) {
							throw new DuplicateArgumentException(operation.getResource().getController(), operation.getConfiguration(), Arrays.asList(index, i));
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
					if (index < 0 || index >= operation.getConfiguration().getParameterAnnotations().length) {
						throw new UndefinedVariableException(operation.getResource().getController(), operation.getConfiguration(), index);
					}
				} else {
					throw new UndefinedVariableException(operation.getResource().getController(), operation.getConfiguration(), name);
				}
			}

			Variable variable = new Variable(name, index, matcher.group(), rule);
			if (variables.add(variable)) {
				expression = expression.replace(matcher.group(), rule);
			} else {
				for (Variable v : variables) {
					if (v.equals(variable)) {
						throw new DuplicateVariableException(operation.getResource().getController(), operation.getConfiguration(), variable, v);
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
			Node<PathExpression, Mapping> branch = new Node<PathExpression, Mapping>(new PathExpression(hierarchy, iterator.hasNext() ? null : command.name()));
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
			result = new Node<PathExpression, Mapping>(new PathExpression(null, command.name()));
			result.setValue(this);
		}
		return result;
	}

	public int compareTo(Mapping o) {
		return expression.compareTo(o.expression) != 0 ? expression.compareTo(o.expression) : command.name().compareTo(o.command.name());
	}

	public Operation getOperation() {
		return operation;
	}

	public Command getCommand() {
		return command;
	}

	public String getDefinition() {
		return definition;
	}

	public String getExpression() {
		return expression;
	}

	@Override
	public String toString() {
		return command.name() + " : " + operation.toString();
	}

	public String toLogString() {
		return command.name() + " : " + definition + " " + operation.toString();
	}

}
