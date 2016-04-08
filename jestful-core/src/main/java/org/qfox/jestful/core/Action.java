package org.qfox.jestful.core;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.qfox.jestful.core.exception.DuplicateExecuteException;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * <p>
 * Description: A request-response job is an Action
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年1月15日 下午8:50:44
 *
 * @since 1.0.0
 */
public class Action {
	private final List<Actor> actors;
	private int index = 0;

	private ListableBeanFactory beanFactory;

	private Object controller;
	private Method method;
	private Parameter[] parameters;
	private Pattern pattern;

	private String command;
	private String URI;
	private String query;
	private String protocol;
	private String version;

	private Message request;
	private Message response;

	private String charset = Charset.defaultCharset().name();

	private Map<Object, Object> extra = new LinkedHashMap<Object, Object>();

	public Action(Collection<Actor> actors) {
		super();
		this.actors = new ArrayList<Actor>(actors);
	}

	/**
	 * pass to the next actor to continue this action
	 * 
	 * @return action result
	 * @throws Exception
	 *             all type exception may be thrown
	 */
	public Object execute() throws Exception {
		if (index == actors.size()) {
			throw new DuplicateExecuteException(this);
		}
		return actors.get(index++).react(this);
	}

	/**
	 * insert the actors to the nearest position
	 * 
	 * @param intruders
	 *            the first actors will execute this action
	 */
	public void intrude(Actor... intruders) {
		actors.addAll(index, Arrays.asList(intruders));
	}

	/**
	 * insert the actors to the nearest position
	 * 
	 * @param intruders
	 *            the first actors will execute this action
	 */
	public void intrude(List<Actor> intruders) {
		actors.addAll(index, intruders);
	}

	public ListableBeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(ListableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public Object getController() {
		return controller;
	}

	public void setController(Object controller) {
		this.controller = controller;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Parameter[] getParameters() {
		return parameters;
	}

	public void setParameters(Parameter[] parameters) {
		this.parameters = parameters;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getURI() {
		return URI;
	}

	public void setURI(String URI) {
		this.URI = URI;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Message getRequest() {
		return request;
	}

	public void setRequest(Message request) {
		this.request = request;
	}

	public Message getResponse() {
		return response;
	}

	public void setResponse(Message response) {
		this.response = response;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Map<Object, Object> getExtra() {
		return extra;
	}

	public void setExtra(Map<Object, Object> extra) {
		this.extra = extra;
	}

}
