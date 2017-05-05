package org.qfox.jestful.server;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.annotation.Jestful;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.nio.charset.UnsupportedCharsetException;
import java.util.*;

/**
 * Created by payne on 2017/4/19.
 * Version: 1.0
 */
public class JestfulWebSupport implements Actor {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected MappingRegistry mappingRegistry;
    protected BeanContainer beanContainer;
    protected VersionComparator versionComparator;
    protected Actor[] plugins;

    protected Charsets acceptCharsets;
    protected Encodings acceptEncodings;
    protected Languages acceptLanguages;

    protected Charsets contentCharsets;
    protected Encodings contentEncodings;
    protected Languages contentLanguages;

    protected boolean allowEncode;
    protected boolean acceptEncode;

    protected String pathEncodeCharset;
    protected String queryEncodeCharset;
    protected String headerEncodeCharset;

    protected void init(ServletContext servletContext, Map<String, String> configuration) {
        ApplicationContext applicationContext = (ApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        {
            String name = configuration.get("beanContainer");
            name = name == null || name.length() == 0 ? "defaultBeanContainer" : name;
            beanContainer = applicationContext.getBean(name, BeanContainer.class);
        }
        {
            String name = configuration.get("mappingRegistry");
            name = name == null || name.length() == 0 ? "jestfulMappingRegistry" : name;
            mappingRegistry = applicationContext.getBean(name, MappingRegistry.class);
        }
        {
            String name = configuration.get("versionComparator");
            name = name == null || name.length() == 0 ? "jestfulVersionComparator" : name;
            versionComparator = applicationContext.getBean(name, VersionComparator.class);
        }
        {
            String plugin = configuration.get("plugin");
            plugin = plugin == null || plugin.length() == 0 ? "jestful" : plugin;
            String[] plugins = plugin.split("\\s*,\\s*");
            this.plugins = new Actor[plugins.length];
            for (int i = 0; i < plugins.length; i++) {
                String[] segments = plugins[i].split("\\s*;\\s*");
                this.plugins[i] = beanContainer.get(segments[0], Actor.class);
                if (this.plugins[i] instanceof Configurable) {
                    Map<String, String> arguments = new LinkedHashMap<String, String>(configuration);
                    for (int j = 1; j < segments.length; j++) {
                        String segment = segments[j];
                        String[] keyvalue = segment.split("\\s*=\\s*");
                        arguments.put(keyvalue[0], keyvalue.length > 1 ? keyvalue[1] : null);
                    }
                    ((Configurable) this.plugins[i]).config(arguments);
                }
            }
        }
        {
            String value = configuration.get("acceptCharset");
            String[] values = value == null || value.length() == 0 ? new String[0] : value.split("\\s*,\\s*");
            this.acceptCharsets = new Charsets(values);
        }
        {
            String value = configuration.get("acceptEncoding");
            String[] values = value == null || value.length() == 0 ? new String[0] : value.split("\\s*,\\s*");
            this.acceptEncodings = new Encodings(values);
        }
        {
            String value = configuration.get("contentLanguage");
            String[] values = value == null || value.length() == 0 ? new String[0] : value.split("\\s*,\\s*");
            this.acceptLanguages = new Languages(values);
        }
        {
            String value = configuration.get("contentCharset");
            String[] values = value == null || value.length() == 0 ? new String[0] : value.split("\\s*,\\s*");
            this.contentCharsets = new Charsets(values);
        }
        {
            String value = configuration.get("contentEncoding");
            String[] values = value == null || value.length() == 0 ? new String[0] : value.split("\\s*,\\s*");
            this.contentEncodings = new Encodings(values);
        }
        {
            String value = configuration.get("contentLanguage");
            String[] values = value == null || value.length() == 0 ? new String[0] : value.split("\\s*,\\s*");
            this.contentLanguages = new Languages(values);
        }
        {
            String allowEncode = configuration.get("allowEncode");
            this.allowEncode = allowEncode == null || allowEncode.length() == 0 ? true : Boolean.valueOf(allowEncode);
            String acceptEncode = configuration.get("acceptEncode");
            this.acceptEncode = acceptEncode == null || acceptEncode.length() == 0 ? true : Boolean.valueOf(acceptEncode);
        }
        {
            String pathEncodeCharset = configuration.get("pathEncodeCharset");
            this.pathEncodeCharset = pathEncodeCharset == null || pathEncodeCharset.length() == 0 ? "UTF-8" : pathEncodeCharset;
            if (!java.nio.charset.Charset.isSupported(this.pathEncodeCharset)) {
                throw new UnsupportedCharsetException(this.pathEncodeCharset);
            }
        }
        {
            String queryEncodeCharset = configuration.get("queryEncodeCharset");
            this.queryEncodeCharset = queryEncodeCharset == null || queryEncodeCharset.length() == 0 ? "UTF-8" : queryEncodeCharset;
            if (!java.nio.charset.Charset.isSupported(this.queryEncodeCharset)) {
                throw new UnsupportedCharsetException(this.queryEncodeCharset);
            }
        }
        {
            String headerEncodeCharset = configuration.get("headerEncodeCharset");
            this.headerEncodeCharset = headerEncodeCharset == null || headerEncodeCharset.length() == 0 ? "UTF-8" : headerEncodeCharset;
            if (!java.nio.charset.Charset.isSupported(this.headerEncodeCharset)) {
                throw new UnsupportedCharsetException(this.headerEncodeCharset);
            }
        }
        Collection<?> controllers = beanContainer.with(Jestful.class).values();
        for (Object controller : controllers) {
            mappingRegistry.register(controller);
        }
        logger.info("resource mapping tree\r\n{}", mappingRegistry);
    }

    protected void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        RequestDescription description = new RequestDescription(httpServletRequest);
        String protocol = description.getProtocol();
        String method = description.getMethod();
        String URI = description.getRequestURI();
        String query = description.getQuery();
        String accept = httpServletRequest.getHeader("Accept");

        Mapping mapping = mappingRegistry.lookup(method, URI, accept, versionComparator).clone();
        Collection<Actor> actors = new ArrayList<Actor>(Arrays.asList(plugins));
        actors.add(this);
        Action action = new Action(beanContainer, actors);

        action.setResource(mapping.getResource());
        action.setMapping(mapping);
        action.setParameters(mapping.getParameters());
        action.setResult(mapping.getResult());
        action.setPattern(mapping.getPattern());

        action.setRestful(mapping.getRestful());
        action.setURI(URI);
        action.setQuery(query);
        action.setProtocol(protocol);
        action.setDispatcher(Dispatcher.valueOf(description.getDispatcherType().name()));

        action.setRequest(new JestfulServletRequest(httpServletRequest));
        action.setResponse(new JestfulServletResponse(httpServletResponse));

        action.setConsumes(mapping.getConsumes());
        action.setProduces(mapping.getProduces());

        action.setAcceptCharsets(acceptCharsets.clone());
        action.setAcceptEncodings(acceptEncodings.clone());
        action.setAcceptLanguages(acceptLanguages.clone());
        action.setContentCharsets(contentCharsets.clone());
        action.setContentEncodings(contentEncodings.clone());
        action.setContentLanguages(contentLanguages.clone());

        action.setAllowEncode(allowEncode);
        action.setAcceptEncode(acceptEncode);

        action.setPathEncodeCharset(pathEncodeCharset);
        action.setQueryEncodeCharset(queryEncodeCharset);
        action.setHeaderEncodeCharset(headerEncodeCharset);

        action.getExtra().put(ServletRequest.class, httpServletRequest);
        action.getExtra().put(ServletResponse.class, httpServletResponse);
        action.getExtra().put(HttpServletRequest.class, httpServletRequest);
        action.getExtra().put(HttpServletResponse.class, httpServletResponse);

        action.execute();
    }

    public Object react(Action action) throws Exception {
        Result result = action.getResult();
        Body body = result.getBody();
        Object controller = action.getResource().getController();
        Method method = action.getMapping().getMethod();
        Parameters parameters = action.getParameters();
        Object[] arguments = parameters.arguments();
        Object value = method.invoke(controller, arguments);
        result.setValue(value);
        body.setValue(value);
        return value;
    }

    public void destroy() {
        Collection<Destroyable> destroyables = beanContainer.find(Destroyable.class).values();
        for (Destroyable destroyable : destroyables) {
            destroyable.destroy();
        }
    }

}
