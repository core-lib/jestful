package org.qfox.jestful.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.tree.Hierarchical;
import org.qfox.jestful.commons.tree.Node;
import org.qfox.jestful.commons.tree.PathExpression;
import org.qfox.jestful.core.annotation.Command;
import org.qfox.jestful.core.exception.DuplicateParameterException;
import org.qfox.jestful.core.exception.IllegalConfigException;
import org.qfox.jestful.core.exception.UndefinedParameterException;

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
public class Mapping extends Configuration implements Hierarchical<PathExpression, Mapping>, Comparable<Mapping> {
	private final Operation operation;
	private final Object controller;
	private final Method method;
	private final Method configuration;
	private final Parameter[] parameters;
	private final Command command;
	private final Set<MediaType> consumes;
	private final Set<MediaType> produces;
	private final String definition;
	private final String expression;
	private final Pattern pattern;

	public Mapping(Operation operation, Annotation[] annotations, Command command) throws IllegalConfigException {
		super(annotations);
		try {
			this.operation = operation;
			this.controller = operation.getController();
			this.method = operation.getMethod();
			this.configuration = operation.getConfiguration();
			this.parameters = extract(method);
			Annotation restful = null;
			for (Annotation annotation : annotations) {
				if (command.equals(annotation.annotationType().getAnnotation(Command.class))) {
					restful = annotation;
				}
			}
			this.command = command;
			this.consumes = new TreeSet<MediaType>();
			String[] consumes = command.hasRequestBody() ? (String[]) restful.annotationType().getMethod("consumes").invoke(restful) : new String[0];
			for (String consume : consumes) {
				this.consumes.add(MediaType.valueOf(consume));
			}
			this.produces = new TreeSet<MediaType>();
			String[] produces = command.hasResponseBody() ? (String[]) restful.annotationType().getMethod("produces").invoke(restful) : new String[0];
			for (String produce : produces) {
				this.produces.add(MediaType.valueOf(produce));
			}
			this.definition = (String) restful.annotationType().getMethod("value").invoke(restful);
			this.expression = bind(definition);
			this.pattern = Pattern.compile(expression);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 将路径上的变量和参数绑定
	 * 
	 * @param path
	 * @return
	 */
	private String bind(String path) {
		Map<String, Parameter> map = new LinkedHashMap<String, Parameter>();
		for (Parameter parameter : parameters) {
			map.put(parameter.getName(), parameter);
		}
		Matcher matcher = Pattern.compile("\\{([^{}]+?)(:([^{}]+?))?\\}").matcher(path);
		int group = 0;
		while (matcher.find()) {
			String name = matcher.group(1);
			String regex = matcher.group(3);
			regex = regex != null ? regex : ".*?";
			if (map.containsKey(name)) {
				Parameter parameter = map.remove(name);
				parameter.setGroup(++group);
				parameter.setRegex(regex);
				path = path.replace(matcher.group(), "(" + regex + ")");
			} else {
				throw new UndefinedParameterException(controller, method, name, path);
			}
		}
		return path;
	}

	/**
	 * 提取方法的所有参数
	 * 
	 * @param method
	 *            方法
	 * @return 方法的所有参数的封装
	 */
	private Parameter[] extract(Method method) throws IllegalConfigException {
		Set<Parameter> parameters = new LinkedHashSet<Parameter>();
		for (int index = 0; index < method.getParameterTypes().length; index++) {
			Parameter parameter = new Parameter(this, method, index);
			if (parameters.contains(parameter)) {
				throw new DuplicateParameterException(controller, method, index);
			} else {
				parameters.add(parameter);
			}
		}
		return parameters.toArray(new Parameter[0]);
	}

	public Node<PathExpression, Mapping> toNode() {
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

	public Object getController() {
		return controller;
	}

	public Method getMethod() {
		return method;
	}

	public Method getConfiguration() {
		return configuration;
	}

	public Parameter[] getParameters() {
		return parameters;
	}

	public Command getCommand() {
		return command;
	}

	public Set<MediaType> getConsumes() {
		return consumes;
	}

	public Set<MediaType> getProduces() {
		return produces;
	}

	public String getDefinition() {
		return definition;
	}

	public String getExpression() {
		return expression;
	}

	public Pattern getPattern() {
		return pattern;
	}

	@Override
	public String toString() {
		return command.name() + " : " + operation.toString();
	}

	public String toLogString() {
		return command.name() + " : " + definition + " " + operation.toString();
	}

}
