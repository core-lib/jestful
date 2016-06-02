package org.qfox.jestful.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Body;
import org.qfox.jestful.core.Charsets;
import org.qfox.jestful.core.Destroyable;
import org.qfox.jestful.core.Encodings;
import org.qfox.jestful.core.Languages;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.Parameters;
import org.qfox.jestful.core.Plugin;
import org.qfox.jestful.core.Result;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.exception.StatusException;
import org.qfox.jestful.core.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

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
 * @date 2016年4月27日 下午3:03:35
 *
 * @since 1.0.0
 */
public class JestfulServletSupport implements Servlet, Actor {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ServletConfig servletConfig;
	private MappingRegistry mappingRegistry;
	private VersionComparator versionComparator;
	private BeanContainer beanContainer;
	private Actor[] plugins;

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

	public void init(ServletConfig config) throws ServletException {
		this.servletConfig = config;

		ServletContext servletContext = config.getServletContext();
		ApplicationContext applicationContext = (ApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		{
			String name = config.getInitParameter("beanContainer");
			name = name == null || name.isEmpty() ? "jestfulBeanContainer" : name;
			beanContainer = applicationContext.getBean(name, BeanContainer.class);
		}
		{
			String name = config.getInitParameter("mappingRegistry");
			name = name == null || name.isEmpty() ? "jestfulMappingRegistry" : name;
			mappingRegistry = applicationContext.getBean(name, MappingRegistry.class);
		}
		{
			String name = config.getInitParameter("versionComparator");
			name = name == null || name.isEmpty() ? "jestfulVersionComparator" : name;
			versionComparator = applicationContext.getBean(name, VersionComparator.class);
		}
		{
			String plugin = config.getInitParameter("plugin");
			plugin = plugin == null || plugin.isEmpty() ? "jestful" : plugin;
			String[] plugins = plugin.split("\\s*,\\s*");
			this.plugins = new Actor[plugins.length];
			for (int i = 0; i < plugins.length; i++) {
				String[] segments = plugins[i].split("\\s*;\\s*");
				this.plugins[i] = beanContainer.get(segments[0], Actor.class);
				if (this.plugins[i] instanceof Plugin) {
					Map<String, String> arguments = new LinkedHashMap<String, String>();
					for (int j = 1; j < segments.length; j++) {
						String segment = segments[j];
						String[] keyvalue = segment.split("\\s*=\\s*");
						arguments.put(keyvalue[0], keyvalue.length > 1 ? keyvalue[1] : null);
					}
					((Plugin) this.plugins[i]).config(arguments);
				}
			}
		}
		{
			String value = config.getInitParameter("acceptCharset");
			String[] values = value == null || value.isEmpty() ? new String[0] : value.split("\\s*,\\s*");
			this.acceptCharsets = new Charsets(values);
		}
		{
			String value = config.getInitParameter("acceptEncoding");
			String[] values = value == null || value.isEmpty() ? new String[0] : value.split("\\s*,\\s*");
			this.acceptEncodings = new Encodings(values);
		}
		{
			String value = config.getInitParameter("contentLanguage");
			String[] values = value == null || value.isEmpty() ? new String[0] : value.split("\\s*,\\s*");
			this.acceptLanguages = new Languages(values);
		}
		{
			String value = config.getInitParameter("contentCharset");
			String[] values = value == null || value.isEmpty() ? new String[0] : value.split("\\s*,\\s*");
			this.contentCharsets = new Charsets(values);
		}
		{
			String value = config.getInitParameter("contentEncoding");
			String[] values = value == null || value.isEmpty() ? new String[0] : value.split("\\s*,\\s*");
			this.contentEncodings = new Encodings(values);
		}
		{
			String value = config.getInitParameter("contentLanguage");
			String[] values = value == null || value.isEmpty() ? new String[0] : value.split("\\s*,\\s*");
			this.contentLanguages = new Languages(values);
		}
		{
			String allowEncode = config.getInitParameter("allowEncode");
			this.allowEncode = allowEncode == null || allowEncode.isEmpty() ? true : Boolean.valueOf(allowEncode);
			String acceptEncode = config.getInitParameter("acceptEncode");
			this.acceptEncode = acceptEncode == null || acceptEncode.isEmpty() ? true : Boolean.valueOf(acceptEncode);
		}
		{
			String pathEncodeCharset = config.getInitParameter("pathEncodeCharset");
			this.pathEncodeCharset = pathEncodeCharset == null || pathEncodeCharset.isEmpty() ? "UTF-8" : pathEncodeCharset;
			if (Charset.isSupported(this.pathEncodeCharset) == false) {
				throw new UnsupportedCharsetException(this.pathEncodeCharset);
			}
		}
		{
			String queryEncodeCharset = config.getInitParameter("queryEncodeCharset");
			this.queryEncodeCharset = queryEncodeCharset == null || queryEncodeCharset.isEmpty() ? "UTF-8" : queryEncodeCharset;
			if (Charset.isSupported(this.queryEncodeCharset) == false) {
				throw new UnsupportedCharsetException(this.queryEncodeCharset);
			}
		}
		{
			String headerEncodeCharset = config.getInitParameter("headerEncodeCharset");
			this.headerEncodeCharset = headerEncodeCharset == null || headerEncodeCharset.isEmpty() ? "UTF-8" : headerEncodeCharset;
			if (Charset.isSupported(this.headerEncodeCharset) == false) {
				throw new UnsupportedCharsetException(this.headerEncodeCharset);
			}
		}

		Collection<?> controllers = beanContainer.with(Jestful.class).values();
		for (Object controller : controllers) {
			mappingRegistry.register(controller);
		}
		logger.info("resource mapping tree\r\n{}", mappingRegistry);
	}

	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String command = httpServletRequest.getMethod();
		String URI = httpServletRequest.getRequestURI();
		String accept = httpServletRequest.getHeader("Accept");
		String query = httpServletRequest.getQueryString();
		String protocol = httpServletRequest.getProtocol();
		try {
			Mapping mapping = mappingRegistry.lookup(command, URI, accept, versionComparator).clone();
			Collection<Actor> actors = new ArrayList<Actor>(Arrays.asList(plugins));
			actors.add(this);
			Action action = new Action(beanContainer, actors);

			action.setResource(mapping.getResource());
			action.setMapping(mapping);
			action.setParameters(mapping.getParameters());
			action.setResult(mapping.getResult());
			action.setPattern(mapping.getPattern());

			action.setRestful(mapping.getRestful());
			action.setURL(httpServletRequest.getRequestURL().toString());
			action.setURI(URI);
			action.setQuery(query);
			action.setProtocol(protocol);

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

			action.execute();
		} catch (StatusException e) {
			httpServletResponse.setStatus(e.getStatus());
			OutputStream out = httpServletResponse.getOutputStream();
			OutputStreamWriter writer = null;
			try {
				writer = new OutputStreamWriter(out, Charset.defaultCharset().name());
				e.responseTo(writer);
				writer.flush();
				out.flush();
			} catch (IOException ioe) {
				logger.warn("{}", ioe);
			} finally {
				IOUtils.close(writer);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
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

	public String getServletInfo() {
		return "jestful servlet support";
	}

	public void destroy() {
		Collection<Destroyable> destroyables = beanContainer.find(Destroyable.class).values();
		for (Destroyable destroyable : destroyables) {
			destroyable.destroy();
		}
	}

}
