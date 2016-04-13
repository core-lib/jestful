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
import org.qfox.jestful.core.exception.AmbiguousMappingException;
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
	private final Resource resource;
	private final Object controller;
	private final Method method;
	private final Method configuration;
	private final Parameter[] parameters;
	private final Result result;
	private final Restful restful;
	private final Set<MediaType> consumes;
	private final Set<MediaType> produces;
	private final String definition;
	private final String expression;
	private final Pattern pattern;

	public Mapping(Resource resource, Object controller, Method method, Method configuration) throws IllegalConfigException {
		super(configuration.getAnnotations());
		try {
			this.resource = resource;
			this.controller = controller;
			this.method = method;
			this.configuration = configuration;
			this.parameters = extract(method);
			this.result = new Result(this, method);
			Annotation[] commands = getAnnotationsWith(Command.class);
			if (commands.length == 1) {
				Annotation restful = getAnnotationWith(Command.class);
				Command command = restful.annotationType().getAnnotation(Command.class);
				this.restful = new Restful(command);
				this.consumes = new TreeSet<MediaType>();
				String[] consumes = command.acceptBody() ? (String[]) restful.annotationType().getMethod("consumes").invoke(restful) : new String[0];
				for (String consume : consumes) {
					this.consumes.add(MediaType.valueOf(consume));
				}
				this.produces = new TreeSet<MediaType>();
				String[] produces = command.returnBody() ? (String[]) restful.annotationType().getMethod("produces").invoke(restful) : new String[0];
				for (String produce : produces) {
					this.produces.add(MediaType.valueOf(produce));
				}
				this.definition = (String) restful.annotationType().getMethod("value").invoke(restful);
				this.expression = bind(definition);
				this.pattern = Pattern.compile(expression);
			} else {
				throw new AmbiguousMappingException("Ambiguous mapping " + configuration.toGenericString() + " which has " + commands.length + " http method kind annotations " + Arrays.toString(commands), controller, method, this);
			}
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
		return parameters.toArray(new Parameter[parameters.size()]);
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
			Node<PathExpression, Mapping> branch = new Node<PathExpression, Mapping>(new PathExpression(hierarchy, iterator.hasNext() ? null : restful.getMethod()));
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
			result = new Node<PathExpression, Mapping>(new PathExpression(null, restful.getMethod()));
			result.setValue(this);
		}
		parent = new Node<PathExpression, Mapping>(new PathExpression(null, restful.getMethod()));
		parent.getBranches().add(result);
		return parent;
	}

	public int compareTo(Mapping o) {
		return expression.compareTo(o.expression) != 0 ? expression.compareTo(o.expression) : restful.getMethod().compareTo(o.restful.getMethod());
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

	public Parameter[] getParameters() {
		return parameters;
	}

	public Result getResult() {
		return result;
	}

	public Restful getRestful() {
		return restful;
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
		return restful.getMethod() + " : " + method.toGenericString();
	}

}