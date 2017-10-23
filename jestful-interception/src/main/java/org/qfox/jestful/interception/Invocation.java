package org.qfox.jestful.interception;

import org.qfox.jestful.core.*;
import org.qfox.jestful.interception.exception.DuplicateInvokeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Payne on 2017/5/5.
 */
public class Invocation {
    private final List<Interceptor> interceptors;
    private volatile int index;
    private volatile Action action;

    Invocation() {
        this.interceptors = new ArrayList<Interceptor>();
    }

    public Object invoke() throws Exception {
        if (index < 0) {
            throw new IllegalStateException("index < 0");
        } else if (index < interceptors.size()) {
            return interceptors.get(index++).intercept(this);
        } else if (index > interceptors.size()) {
            throw new DuplicateInvokeException(this);
        } else {
            index++;
            return action.execute();
        }
    }

    void reset(Action action) {
        this.action = action;
        this.interceptors.clear();
        this.index = 0;
    }

    void accept(Interceptor interceptor) {
        this.interceptors.add(interceptor);
    }

    public void intrude(Actor... intruders) {
        action.intrude(intruders);
    }

    public void intrude(List<Actor> intruders) {
        action.intrude(intruders);
    }

    public BeanContainer getBeanContainer() {
        return action.getBeanContainer();
    }

    public Resource getResource() {
        return action.getResource();
    }

    public void setResource(Resource resource) {
        action.setResource(resource);
    }

    public Mapping getMapping() {
        return action.getMapping();
    }

    public void setMapping(Mapping mapping) {
        action.setMapping(mapping);
    }

    public Parameters getParameters() {
        return action.getParameters();
    }

    public void setParameters(Parameters parameters) {
        action.setParameters(parameters);
    }

    public Result getResult() {
        return action.getResult();
    }

    public void setResult(Result result) {
        action.setResult(result);
    }

    public Pattern getPattern() {
        return action.getPattern();
    }

    public void setPattern(Pattern pattern) {
        action.setPattern(pattern);
    }

    public Restful getRestful() {
        return action.getRestful();
    }

    public void setRestful(Restful restful) {
        action.setRestful(restful);
    }

    public String getURL() {
        return action.getURL();
    }

    public void setURL(String URL) {
        action.setURL(URL);
    }

    public String getURI() {
        return action.getURI();
    }

    public void setURI(String URI) {
        action.setURI(URI);
    }

    public String getQuery() {
        return action.getQuery();
    }

    public void setQuery(String query) {
        action.setQuery(query);
    }

    public String getProtocol() {
        return action.getProtocol();
    }

    public void setProtocol(String protocol) {
        action.setProtocol(protocol);
    }

    public String getHost() {
        return action.getHostname();
    }

    public void setHost(String host) {
        action.setHostname(host);
    }

    public Integer getPort() {
        return action.getPort();
    }

    public void setPort(Integer port) {
        action.setPort(port);
    }

    public String getRoute() {
        return action.getRoute();
    }

    public void setRoute(String route) {
        action.setRoute(route);
    }

    public Dispatcher getDispatcher() {
        return action.getDispatcher();
    }

    public void setDispatcher(Dispatcher dispatcher) {
        action.setDispatcher(dispatcher);
    }

    public Request getRequest() {
        return action.getRequest();
    }

    public void setRequest(Request request) {
        action.setRequest(request);
    }

    public Response getResponse() {
        return action.getResponse();
    }

    public void setResponse(Response response) {
        action.setResponse(response);
    }

    public Map<String, String[]> getQueries() {
        return action.getQueries();
    }

    public void setQueries(Map<String, String[]> queries) {
        action.setQueries(queries);
    }

    public Map<String, String[]> getHeaders() {
        return action.getHeaders();
    }

    public void setHeaders(Map<String, String[]> headers) {
        action.setHeaders(headers);
    }

    public Map<String, String[]> getCookies() {
        return action.getCookies();
    }

    public void setCookies(Map<String, String[]> cookies) {
        action.setCookies(cookies);
    }

    public Accepts getConsumes() {
        return action.getConsumes();
    }

    public void setConsumes(Accepts consumes) {
        action.setConsumes(consumes);
    }

    public Accepts getProduces() {
        return action.getProduces();
    }

    public void setProduces(Accepts produces) {
        action.setProduces(produces);
    }

    public Charsets getAcceptCharsets() {
        return action.getAcceptCharsets();
    }

    public void setAcceptCharsets(Charsets acceptCharsets) {
        action.setAcceptCharsets(acceptCharsets);
    }

    public Encodings getAcceptEncodings() {
        return action.getAcceptEncodings();
    }

    public void setAcceptEncodings(Encodings acceptEncodings) {
        action.setAcceptEncodings(acceptEncodings);
    }

    public Languages getAcceptLanguages() {
        return action.getAcceptLanguages();
    }

    public void setAcceptLanguages(Languages acceptLanguages) {
        action.setAcceptLanguages(acceptLanguages);
    }

    public Charsets getContentCharsets() {
        return action.getContentCharsets();
    }

    public void setContentCharsets(Charsets contentCharsets) {
        action.setContentCharsets(contentCharsets);
    }

    public Encodings getContentEncodings() {
        return action.getContentEncodings();
    }

    public void setContentEncodings(Encodings contentEncodings) {
        action.setContentEncodings(contentEncodings);
    }

    public Languages getContentLanguages() {
        return action.getContentLanguages();
    }

    public void setContentLanguages(Languages contentLanguages) {
        action.setContentLanguages(contentLanguages);
    }

    public boolean isAllowEncode() {
        return action.isAllowEncode();
    }

    public void setAllowEncode(boolean allowEncode) {
        action.setAllowEncode(allowEncode);
    }

    public boolean isAcceptEncode() {
        return action.isAcceptEncode();
    }

    public void setAcceptEncode(boolean acceptEncode) {
        action.setAcceptEncode(acceptEncode);
    }

    public String getPathEncodeCharset() {
        return action.getPathEncodeCharset();
    }

    public void setPathEncodeCharset(String pathEncodeCharset) {
        action.setPathEncodeCharset(pathEncodeCharset);
    }

    public String getQueryEncodeCharset() {
        return action.getQueryEncodeCharset();
    }

    public void setQueryEncodeCharset(String queryEncodeCharset) {
        action.setQueryEncodeCharset(queryEncodeCharset);
    }

    public String getHeaderEncodeCharset() {
        return action.getHeaderEncodeCharset();
    }

    public void setHeaderEncodeCharset(String headerEncodeCharset) {
        action.setHeaderEncodeCharset(headerEncodeCharset);
    }

    public Map<Object, Object> getExtra() {
        return action.getExtra();
    }

    public void setExtra(Map<Object, Object> extra) {
        action.setExtra(extra);
    }
}
