package org.qfox.jestful.core;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.qfox.jestful.core.exception.DuplicateExecuteException;

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
	private final BeanContainer beanContainer;
	private final List<Actor> actors;
	private int index = 0;

	private Resource resource;
	private Mapping mapping;
	private Parameters parameters;
	private Result result;
	private Pattern pattern;

	private Restful restful;
	private String URL;
	private String URI;
	private String query;
	private String protocol;
	private String version;
	private String host;
	private Integer port;
	private String route;

	private Request request;
	private Response response;

	private Accepts consumes;
	private Accepts produces;

	private String charset = Charset.defaultCharset().name();

	private List<String> acceptCharsets;
	private List<String> acceptEncodings;
	private List<String> acceptLanguages;

	private List<String> contentCharsets;
	private List<String> contentEncodings;
	private List<String> contentLanguages;

	private boolean acceptEncode;
	private boolean allowEncode;

	private Map<Object, Object> extra = new LinkedHashMap<Object, Object>();

	public Action(BeanContainer beanContainer, Collection<Actor> actors) {
		super();
		this.beanContainer = beanContainer;
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

	public BeanContainer getBeanContainer() {
		return beanContainer;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Mapping getMapping() {
		return mapping;
	}

	public void setMapping(Mapping mapping) {
		this.mapping = mapping;
	}

	public Parameters getParameters() {
		return parameters;
	}

	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public Restful getRestful() {
		return restful;
	}

	public void setRestful(Restful restful) {
		this.restful = restful;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
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

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public Accepts getConsumes() {
		return consumes;
	}

	public void setConsumes(Accepts consumes) {
		this.consumes = consumes;
	}

	public Accepts getProduces() {
		return produces;
	}

	public void setProduces(Accepts produces) {
		this.produces = produces;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public List<String> getAcceptCharsets() {
		return acceptCharsets;
	}

	public void setAcceptCharsets(List<String> acceptCharsets) {
		this.acceptCharsets = acceptCharsets;
	}

	public List<String> getAcceptEncodings() {
		return acceptEncodings;
	}

	public void setAcceptEncodings(List<String> acceptEncodings) {
		this.acceptEncodings = acceptEncodings;
	}

	public List<String> getAcceptLanguages() {
		return acceptLanguages;
	}

	public void setAcceptLanguages(List<String> acceptLanguages) {
		this.acceptLanguages = acceptLanguages;
	}

	public List<String> getContentCharsets() {
		return contentCharsets;
	}

	public void setContentCharsets(List<String> contentCharsets) {
		this.contentCharsets = contentCharsets;
	}

	public List<String> getContentEncodings() {
		return contentEncodings;
	}

	public void setContentEncodings(List<String> contentEncodings) {
		this.contentEncodings = contentEncodings;
	}

	public List<String> getContentLanguages() {
		return contentLanguages;
	}

	public void setContentLanguages(List<String> contentLanguages) {
		this.contentLanguages = contentLanguages;
	}

	public boolean isAcceptEncode() {
		return acceptEncode;
	}

	public void setAcceptEncode(boolean acceptEncode) {
		this.acceptEncode = acceptEncode;
	}

	public boolean isAllowEncode() {
		return allowEncode;
	}

	public void setAllowEncode(boolean allowEncode) {
		this.allowEncode = allowEncode;
	}

	public Map<Object, Object> getExtra() {
		return extra;
	}

	public void setExtra(Map<Object, Object> extra) {
		this.extra = extra;
	}

}
