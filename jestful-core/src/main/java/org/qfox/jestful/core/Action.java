package org.qfox.jestful.core;

import org.qfox.jestful.core.exception.DuplicateExecuteException;

import java.util.*;
import java.util.regex.Pattern;

/**
 * <p>
 * Description: A request-response job is an Action
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年1月15日 下午8:50:44
 * @since 1.0.0
 */
public class Action {
    private final BeanContainer beanContainer;
    private final List<Actor> actors;
    private final List<Actor> forePlugins;
    private final List<Actor> backPlugins;
    private volatile int index = 0;
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
    private String hostname;
    private Integer port;
    private String route;
    private Dispatcher dispatcher = Dispatcher.REQUEST;

    private Request request;
    private Response response;

    private Map<String, String[]> queries = Collections.emptyMap();
    private Map<String, String[]> headers = Collections.emptyMap();
    private Map<String, String[]> cookies = Collections.emptyMap();

    private Accepts consumes;
    private Accepts produces;

    private Charsets acceptCharsets;
    private Encodings acceptEncodings;
    private Languages acceptLanguages;

    private Charsets contentCharsets;
    private Encodings contentEncodings;
    private Languages contentLanguages;

    private boolean allowEncode;
    private boolean acceptEncode;

    private String pathEncodeCharset;
    private String queryEncodeCharset;
    private String headerEncodeCharset;

    private Map<Object, Object> extra = new LinkedHashMap<Object, Object>();

    public Action(BeanContainer beanContainer, Collection<Actor> actors) {
        this(beanContainer, actors, null, null);
    }

    public Action(BeanContainer beanContainer, Collection<Actor> actors, Collection<Actor> forePlugins, Collection<Actor> backPlugins) {
        this.beanContainer = beanContainer;
        this.actors = new ArrayList<Actor>(actors);
        this.forePlugins = forePlugins == null ? Collections.<Actor>emptyList() : Collections.unmodifiableList(new ArrayList<Actor>(forePlugins));
        this.backPlugins = backPlugins == null ? Collections.<Actor>emptyList() : Collections.unmodifiableList(new ArrayList<Actor>(backPlugins));
    }

    /**
     * pass to the next actor to continue this action
     *
     * @return action result
     * @throws Exception all type exception may be thrown
     */
    public Object execute() throws Exception {
        if (index >= actors.size()) {
            throw new DuplicateExecuteException(this);
        }
        return actors.get(index++).react(this);
    }

    /**
     * insert the actors to the nearest position
     *
     * @param intruders the first actors will execute this action
     */
    public void intrude(Actor... intruders) {
        actors.addAll(index, Arrays.asList(intruders));
    }

    /**
     * insert the actors to the nearest position
     *
     * @param intruders the first actors will execute this action
     */
    public void intrude(List<Actor> intruders) {
        actors.addAll(index, intruders);
    }

    public BeanContainer getBeanContainer() {
        return beanContainer;
    }

    public List<Actor> getForePlugins() {
        return forePlugins;
    }

    public List<Actor> getBackPlugins() {
        return backPlugins;
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

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
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

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
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

    public Map<String, String[]> getQueries() {
        return queries;
    }

    public void setQueries(Map<String, String[]> queries) {
        this.queries = queries;
    }

    public Map<String, String[]> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String[]> headers) {
        this.headers = headers;
    }

    public Map<String, String[]> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String[]> cookies) {
        this.cookies = cookies;
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

    public Charsets getAcceptCharsets() {
        return acceptCharsets;
    }

    public void setAcceptCharsets(Charsets acceptCharsets) {
        this.acceptCharsets = acceptCharsets;
    }

    public Encodings getAcceptEncodings() {
        return acceptEncodings;
    }

    public void setAcceptEncodings(Encodings acceptEncodings) {
        this.acceptEncodings = acceptEncodings;
    }

    public Languages getAcceptLanguages() {
        return acceptLanguages;
    }

    public void setAcceptLanguages(Languages acceptLanguages) {
        this.acceptLanguages = acceptLanguages;
    }

    public Charsets getContentCharsets() {
        return contentCharsets;
    }

    public void setContentCharsets(Charsets contentCharsets) {
        this.contentCharsets = contentCharsets;
    }

    public Encodings getContentEncodings() {
        return contentEncodings;
    }

    public void setContentEncodings(Encodings contentEncodings) {
        this.contentEncodings = contentEncodings;
    }

    public Languages getContentLanguages() {
        return contentLanguages;
    }

    public void setContentLanguages(Languages contentLanguages) {
        this.contentLanguages = contentLanguages;
    }

    public boolean isAllowEncode() {
        return allowEncode;
    }

    public void setAllowEncode(boolean allowEncode) {
        this.allowEncode = allowEncode;
    }

    public boolean isAcceptEncode() {
        return acceptEncode;
    }

    public void setAcceptEncode(boolean acceptEncode) {
        this.acceptEncode = acceptEncode;
    }

    public String getPathEncodeCharset() {
        return pathEncodeCharset;
    }

    public void setPathEncodeCharset(String pathEncodeCharset) {
        this.pathEncodeCharset = pathEncodeCharset;
    }

    public String getQueryEncodeCharset() {
        return queryEncodeCharset;
    }

    public void setQueryEncodeCharset(String queryEncodeCharset) {
        this.queryEncodeCharset = queryEncodeCharset;
    }

    public String getHeaderEncodeCharset() {
        return headerEncodeCharset;
    }

    public void setHeaderEncodeCharset(String headerEncodeCharset) {
        this.headerEncodeCharset = headerEncodeCharset;
    }

    public Map<Object, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<Object, Object> extra) {
        this.extra = extra;
    }

}
