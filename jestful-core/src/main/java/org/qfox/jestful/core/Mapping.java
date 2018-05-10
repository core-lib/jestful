package org.qfox.jestful.core;

import org.qfox.jestful.commons.tree.Hierarchical;
import org.qfox.jestful.commons.tree.Node;
import org.qfox.jestful.commons.tree.PathExpression;
import org.qfox.jestful.core.annotation.Function;
import org.qfox.jestful.core.annotation.Version;
import org.qfox.jestful.core.exception.AmbiguousMappingException;
import org.qfox.jestful.core.exception.DuplicateParameterException;
import org.qfox.jestful.core.exception.IllegalConfigException;
import org.qfox.jestful.core.exception.UndefinedParameterException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * @date 2016年3月31日 上午11:32:21
 * @since 1.0.0
 */
public class Mapping extends Configuration implements Hierarchical<PathExpression, Mapping>, Comparable<Mapping>, Cloneable {
    private final Resource resource;
    private final Object controller;
    private final Method method;
    private final Method configuration;
    private final Parameters parameters;
    private final Result result;
    private final Restful restful;
    private final Accepts consumes;
    private final Accepts produces;
    private final String expression;
    private final String regex;
    private final Pattern pattern;
    private final String version;

    public Mapping(Resource resource, Parameters parameters, Result result, Restful restful, Accepts consumes, Accepts produces) {
        super(new Annotation[0]);
        this.resource = resource;
        this.controller = resource.getController();
        this.method = null;
        this.configuration = null;
        this.parameters = parameters;
        this.result = result;
        this.restful = restful;
        this.consumes = consumes;
        this.produces = produces;
        this.expression = "";
        this.regex = "";
        this.pattern = Pattern.compile("");
        this.version = null;
    }

    public Mapping(Resource resource, Object controller, Method method, Method configuration) throws IllegalConfigException {
        super(configuration.getAnnotations());
        try {
            this.resource = resource;
            this.controller = controller;
            this.method = method;
            this.configuration = configuration;
            this.parameters = extract(method);
            this.result = new Result(this, method);
            Annotation[] functions = getAnnotationsWith(Function.class);
            if (functions.length == 1) {
                Annotation restful = functions[0];
                Function function = restful.annotationType().getAnnotation(Function.class);
                this.restful = new Restful(function);

                Set<MediaType> consumeMediaTypes = new TreeSet<MediaType>();
                String[] consumes = function.acceptBody()
                        ? (String[]) restful.annotationType().getMethod("consumes").invoke(restful)
                        : new String[0];
                for (String consume : consumes) consumeMediaTypes.add(MediaType.valueOf(consume));
                this.consumes = new Accepts(consumeMediaTypes);

                Set<MediaType> produceMediaTypes = new TreeSet<MediaType>();
                String[] produces = function.returnBody()
                        ? (String[]) restful.annotationType().getMethod("produces").invoke(restful)
                        : new String[0];
                for (String produce : produces) produceMediaTypes.add(MediaType.valueOf(produce));
                this.produces = new Accepts(produceMediaTypes);

                String value = restful.annotationType().getMethod("value").invoke(restful).toString();
                if (!value.equals("/")) value = ("/" + value).replaceAll("/+", "/");
                if (!value.equals("/")) value = value.replaceAll("/+$", "");
                this.expression = value;
                this.regex = bind(resource.getExpression() + expression);
                this.pattern = Pattern.compile(regex);

                Version version = this.isAnnotationPresent(Version.class)
                        ? this.getAnnotation(Version.class)
                        : resource.isAnnotationPresent(Version.class)
                        ? resource.getAnnotation(Version.class)
                        : null;
                this.version = version != null ? version.value() : null;
            } else {
                String message = String.format(
                        "Ambiguous mapping %s which has %d command kind annotations %s",
                        configuration.toGenericString(),
                        functions.length,
                        Arrays.toString(functions)
                );
                throw new AmbiguousMappingException(message, controller, method, this);
            }
        } catch (IllegalConfigException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 将路径上的变量和参数绑定
     *
     * @param path 映射路径
     * @return 正则化的路径表达式
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
            regex = regex != null ? regex : "[^/]*?";
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
     * @param method 方法
     * @return 方法的所有参数的封装
     */
    private Parameters extract(Method method) throws IllegalConfigException {
        Set<Parameter> parameters = new LinkedHashSet<Parameter>();
        for (int index = 0; index < method.getParameterTypes().length; index++) {
            Parameter parameter = new Parameter(this, method, index);
            if (parameters.contains(parameter)) throw new DuplicateParameterException(controller, method, index);
            else parameters.add(parameter);
        }
        return new Parameters(parameters);
    }

    public Node<PathExpression, Mapping> toNode() {
        String[] hierarchies = regex.split("/+");
        Iterator<String> iterator = Arrays.asList(hierarchies).iterator();
        Node<PathExpression, Mapping> result = null;
        Node<PathExpression, Mapping> parent = null;
        while (iterator.hasNext()) {
            String hierarchy = iterator.next();
            if (hierarchy.length() == 0) {
                continue;
            }
            PathExpression expression = new PathExpression(hierarchy, iterator.hasNext() ? null : restful.getMethod(), version);
            Node<PathExpression, Mapping> branch = new Node<PathExpression, Mapping>(expression);
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
            result = new Node<PathExpression, Mapping>(new PathExpression(null, restful.getMethod(), version));
            result.setValue(this);
        }
        parent = new Node<PathExpression, Mapping>(new PathExpression(null, restful.getMethod(), version));
        parent.getBranches().add(result);
        return parent;
    }

    public int compareTo(Mapping o) {
        int comparation;
        if ((comparation = regex.compareTo(o.regex)) != 0) {
            return comparation;
        } else if ((comparation = restful.getMethod().compareTo(o.restful.getMethod())) != 0) {
            return comparation;
        } else {
            // 有版本号比没版本号更新
            if (version == null && o.version == null) return 0;
            else if (version == null) return 1;
            else if (o.version == null) return -1;
            else return version.compareTo(o.version);
        }
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public Mapping clone() {
        return new Mapping(resource, controller, method, configuration);
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

    public Parameters getParameters() {
        return parameters;
    }

    public Result getResult() {
        return result;
    }

    public Restful getRestful() {
        return restful;
    }

    public Accepts getConsumes() {
        return consumes;
    }

    public Accepts getProduces() {
        return produces;
    }

    public String getExpression() {
        return expression;
    }

    public String getRegex() {
        return regex;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return (version != null ? "(" + version + ")" + " " : "") + restful.getMethod() + " : " + method.toGenericString();
    }

}
