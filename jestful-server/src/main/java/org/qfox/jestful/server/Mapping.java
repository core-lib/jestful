package org.qfox.jestful.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.qfox.jestful.core.annotation.Method;
import org.qfox.jestful.server.exception.AlreadyValuedException;
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
public class Mapping implements Hierarchical<PathExpression, Mapping> {
	private static final Pattern PATTERN = Pattern.compile("\\{(?<name>[^{}]+?)(:(?<rule>[^{}]+?))?\\}");

	private final Operation operation;
	private final Method method;
	private final String definition;
	private final String expression;
	private final List<Variable> variables = new ArrayList<Variable>();

	public Mapping(Operation operation, Method method, String definition) {
		super();
		this.operation = operation;
		this.method = method;
		this.definition = definition;
		String expression = definition;
		Matcher matcher = PATTERN.matcher(definition);
		int index = 0;
		while (matcher.find()) {
			String name = matcher.group("name");
			String rule = matcher.group("rule");
			rule = rule != null ? rule : "[^/]+";
			Variable variable = new Variable(name, index++, matcher.group(), rule);
			this.variables.add(variable);
			expression = expression.replace(matcher.group(), rule);
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
