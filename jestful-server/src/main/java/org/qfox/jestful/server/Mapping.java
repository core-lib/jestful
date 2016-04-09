package org.qfox.jestful.server;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Source;
import org.qfox.jestful.core.annotation.Argument;
import org.qfox.jestful.core.annotation.Command;
import org.qfox.jestful.core.exception.JestfulRuntimeException;
import org.qfox.jestful.server.exception.AlreadyValuedException;
import org.qfox.jestful.server.exception.DuplicateParameterException;
import org.qfox.jestful.server.exception.IllegalConfigException;
import org.qfox.jestful.server.exception.NonuniqueSourceException;
import org.qfox.jestful.server.exception.UnassailableParameterException;
import org.qfox.jestful.server.exception.UndefinedParameterException;
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
	private final Operation operation;
	private final Object controller;
	private final Method method;
	private final Method configuration;
	private final List<Parameter> parameters;
	private final Command command;
	private final Set<MediaType> consumes;
	private final Set<MediaType> produces;
	private final String definition;
	private final String expression;
	private final Pattern pattern;

	public Mapping(Operation operation, Annotation annotation) throws IllegalConfigException {
		super();
		try {
			this.operation = operation;
			this.controller = operation.getController();
			this.method = operation.getMethod();
			this.configuration = operation.getMethod();
			this.parameters = extract(method);
			this.command = annotation.annotationType().getAnnotation(Command.class);
			this.consumes = new TreeSet<MediaType>();
			String[] consumes = command.hasRequestBody() ? (String[]) annotation.annotationType().getMethod("consumes").invoke(annotation) : new String[0];
			for (String consume : consumes) {
				this.consumes.add(MediaType.valueOf(consume));
			}
			this.produces = new TreeSet<MediaType>();
			String[] produces = command.hasResponseBody() ? (String[]) annotation.annotationType().getMethod("produces").invoke(annotation) : new String[0];
			for (String produce : produces) {
				this.produces.add(MediaType.valueOf(produce));
			}
			this.definition = (String) annotation.annotationType().getMethod("value").invoke(annotation);
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
			if (parameter.getSource() != Source.PATH) {
				continue;
			}
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
		if (map.isEmpty()) {
			return path;
		} else {
			throw new UnassailableParameterException(controller, method, map.values());
		}
	}

	/**
	 * 提取指定下标的方法参数
	 * 
	 * @param method
	 *            方法
	 * @param index
	 *            参数下标
	 * @return 参数的封装
	 */
	private Parameter extract(Method method, int index) throws IllegalConfigException {
		Type type = method.getGenericParameterTypes()[index];
		String name = null;
		Source source = null;
		Annotation[] annotations = method.getParameterAnnotations()[index];
		for (Annotation annotation : annotations) {
			Argument argument = annotation.annotationType().getAnnotation(Argument.class);
			if (argument == null) {
				continue;
			}
			if (name != null || source != null) {
				throw new NonuniqueSourceException(controller, method, index, Arrays.asList(source, argument.value()));
			}
			try {
				name = annotation.annotationType().getMethod("value").invoke(annotation).toString();
				source = argument.value();
			} catch (Exception e) {
				throw new JestfulRuntimeException(e);
			}
		}
		Parameter parameter = new Parameter(method, type, index, name == null || name.isEmpty() ? String.valueOf(index) : name, source);
		return parameter;
	}

	/**
	 * 提取方法的所有参数
	 * 
	 * @param method
	 *            方法
	 * @return 方法的所有参数的封装
	 */
	private List<Parameter> extract(Method method) throws IllegalConfigException {
		List<Parameter> parameters = new ArrayList<Parameter>();
		for (int index = 0; index < method.getGenericParameterTypes().length; index++) {
			Parameter parameter = extract(method, index);
			if (parameters.contains(parameter)) {
				throw new DuplicateParameterException(controller, method, index);
			} else {
				parameters.add(parameter);
			}
		}
		return parameters;
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

	public Object getController() {
		return controller;
	}

	public Method getMethod() {
		return method;
	}

	public Method getConfiguration() {
		return configuration;
	}

	public List<Parameter> getParameters() {
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
