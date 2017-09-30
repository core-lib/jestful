package org.qfox.jestful.client;

import org.qfox.jestful.core.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by yangchangpei on 17/9/29.
 */
public class ActionDelegate extends Action {
    protected final Action action;

    protected ActionDelegate(Action action) {
        this.action = action;
    }

    @Override
    public Object execute() throws Exception {
        return action.execute();
    }

    @Override
    public void intrude(Actor... intruders) {
        action.intrude(intruders);
    }

    @Override
    public void intrude(List<Actor> intruders) {
        action.intrude(intruders);
    }

    @Override
    public BeanContainer getBeanContainer() {
        return action.getBeanContainer();
    }

    @Override
    public Resource getResource() {
        return action.getResource();
    }

    @Override
    public void setResource(Resource resource) {
        action.setResource(resource);
    }

    @Override
    public Mapping getMapping() {
        return action.getMapping();
    }

    @Override
    public void setMapping(Mapping mapping) {
        action.setMapping(mapping);
    }

    @Override
    public Parameters getParameters() {
        return action.getParameters();
    }

    @Override
    public void setParameters(Parameters parameters) {
        action.setParameters(parameters);
    }

    @Override
    public Result getResult() {
        return action.getResult();
    }

    @Override
    public void setResult(Result result) {
        action.setResult(result);
    }

    @Override
    public Pattern getPattern() {
        return action.getPattern();
    }

    @Override
    public void setPattern(Pattern pattern) {
        action.setPattern(pattern);
    }

    @Override
    public Restful getRestful() {
        return action.getRestful();
    }

    @Override
    public void setRestful(Restful restful) {
        action.setRestful(restful);
    }

    @Override
    public String getURL() {
        return action.getURL();
    }

    @Override
    public void setURL(String URL) {
        action.setURL(URL);
    }

    @Override
    public String getURI() {
        return action.getURI();
    }

    @Override
    public void setURI(String URI) {
        action.setURI(URI);
    }

    @Override
    public String getQuery() {
        return action.getQuery();
    }

    @Override
    public void setQuery(String query) {
        action.setQuery(query);
    }

    @Override
    public String getProtocol() {
        return action.getProtocol();
    }

    @Override
    public void setProtocol(String protocol) {
        action.setProtocol(protocol);
    }

    @Override
    public String getHost() {
        return action.getHost();
    }

    @Override
    public void setHost(String host) {
        action.setHost(host);
    }

    @Override
    public Integer getPort() {
        return action.getPort();
    }

    @Override
    public void setPort(Integer port) {
        action.setPort(port);
    }

    @Override
    public String getRoute() {
        return action.getRoute();
    }

    @Override
    public void setRoute(String route) {
        action.setRoute(route);
    }

    @Override
    public Dispatcher getDispatcher() {
        return action.getDispatcher();
    }

    @Override
    public void setDispatcher(Dispatcher dispatcher) {
        action.setDispatcher(dispatcher);
    }

    @Override
    public Request getRequest() {
        return action.getRequest();
    }

    @Override
    public void setRequest(Request request) {
        action.setRequest(request);
    }

    @Override
    public Response getResponse() {
        return action.getResponse();
    }

    @Override
    public void setResponse(Response response) {
        action.setResponse(response);
    }

    @Override
    public Map<String, String[]> getQueries() {
        return action.getQueries();
    }

    @Override
    public void setQueries(Map<String, String[]> queries) {
        action.setQueries(queries);
    }

    @Override
    public Map<String, String[]> getHeaders() {
        return action.getHeaders();
    }

    @Override
    public void setHeaders(Map<String, String[]> headers) {
        action.setHeaders(headers);
    }

    @Override
    public Map<String, String[]> getCookies() {
        return action.getCookies();
    }

    @Override
    public void setCookies(Map<String, String[]> cookies) {
        action.setCookies(cookies);
    }

    @Override
    public Accepts getConsumes() {
        return action.getConsumes();
    }

    @Override
    public void setConsumes(Accepts consumes) {
        action.setConsumes(consumes);
    }

    @Override
    public Accepts getProduces() {
        return action.getProduces();
    }

    @Override
    public void setProduces(Accepts produces) {
        action.setProduces(produces);
    }

    @Override
    public Charsets getAcceptCharsets() {
        return action.getAcceptCharsets();
    }

    @Override
    public void setAcceptCharsets(Charsets acceptCharsets) {
        action.setAcceptCharsets(acceptCharsets);
    }

    @Override
    public Encodings getAcceptEncodings() {
        return action.getAcceptEncodings();
    }

    @Override
    public void setAcceptEncodings(Encodings acceptEncodings) {
        action.setAcceptEncodings(acceptEncodings);
    }

    @Override
    public Languages getAcceptLanguages() {
        return action.getAcceptLanguages();
    }

    @Override
    public void setAcceptLanguages(Languages acceptLanguages) {
        action.setAcceptLanguages(acceptLanguages);
    }

    @Override
    public Charsets getContentCharsets() {
        return action.getContentCharsets();
    }

    @Override
    public void setContentCharsets(Charsets contentCharsets) {
        action.setContentCharsets(contentCharsets);
    }

    @Override
    public Encodings getContentEncodings() {
        return action.getContentEncodings();
    }

    @Override
    public void setContentEncodings(Encodings contentEncodings) {
        action.setContentEncodings(contentEncodings);
    }

    @Override
    public Languages getContentLanguages() {
        return action.getContentLanguages();
    }

    @Override
    public void setContentLanguages(Languages contentLanguages) {
        action.setContentLanguages(contentLanguages);
    }

    @Override
    public boolean isAllowEncode() {
        return action.isAllowEncode();
    }

    @Override
    public void setAllowEncode(boolean allowEncode) {
        action.setAllowEncode(allowEncode);
    }

    @Override
    public boolean isAcceptEncode() {
        return action.isAcceptEncode();
    }

    @Override
    public void setAcceptEncode(boolean acceptEncode) {
        action.setAcceptEncode(acceptEncode);
    }

    @Override
    public String getPathEncodeCharset() {
        return action.getPathEncodeCharset();
    }

    @Override
    public void setPathEncodeCharset(String pathEncodeCharset) {
        action.setPathEncodeCharset(pathEncodeCharset);
    }

    @Override
    public String getQueryEncodeCharset() {
        return action.getQueryEncodeCharset();
    }

    @Override
    public void setQueryEncodeCharset(String queryEncodeCharset) {
        action.setQueryEncodeCharset(queryEncodeCharset);
    }

    @Override
    public String getHeaderEncodeCharset() {
        return action.getHeaderEncodeCharset();
    }

    @Override
    public void setHeaderEncodeCharset(String headerEncodeCharset) {
        action.setHeaderEncodeCharset(headerEncodeCharset);
    }

    @Override
    public Map<Object, Object> getExtra() {
        return action.getExtra();
    }

    @Override
    public void setExtra(Map<Object, Object> extra) {
        action.setExtra(extra);
    }
}
