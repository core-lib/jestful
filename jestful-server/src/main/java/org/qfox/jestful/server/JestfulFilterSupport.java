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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qfox.jestful.commons.io.IOUtils;
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
import org.qfox.jestful.server.exception.NotFoundStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

/**
 * <p>
 * Description: 框架过滤器, 该过滤器只拦截和处理已经映射的路径, 如果不路径不存在则交给下一个过滤器处理, 本身不报404错误
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月7日 上午11:37:48
 *
 * @since 1.0.0
 */
public class JestfulFilterSupport implements Filter, Actor {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private MappingRegistry mappingRegistry;
	private BeanContainer beanContainer;
	private VersionComparator versionComparator;
	private Actor[] plugins;

	private Charsets acceptCharsets;
	private Encodings acceptEncodings;
	private Languages acceptLanguages;

	private Charsets contentCharsets;
	private Encodings contentEncodings;
	private Languages contentLanguages;

	private boolean acceptEncode;
	private boolean allowEncode;
	
	private String pathEncoding;
	private String queryEncoding;
	private String headerEncoding;

	public void init(FilterConfig config) throws ServletException {
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
			String pathEncoding = config.getInitParameter("pathEncoding");
			this.pathEncoding = pathEncoding == null || pathEncoding.isEmpty() ? "UTF-8" : pathEncoding;
			if(Charset.isSupported(this.pathEncoding) == false){
				throw new UnsupportedCharsetException(this.pathEncoding);
			}
		}
		{
			String queryEncoding = config.getInitParameter("queryEncoding");
			this.queryEncoding = queryEncoding == null || queryEncoding.isEmpty() ? "UTF-8" : queryEncoding;
			if(Charset.isSupported(this.queryEncoding) == false){
				throw new UnsupportedCharsetException(this.queryEncoding);
			}
		}
		{
			String headerEncoding = config.getInitParameter("headerEncoding");
			this.headerEncoding = headerEncoding == null || headerEncoding.isEmpty() ? "UTF-8" : headerEncoding;
			if(Charset.isSupported(this.headerEncoding) == false){
				throw new UnsupportedCharsetException(this.headerEncoding);
			}
		}
		Collection<?> controllers = beanContainer.with(Jestful.class).values();
		for (Object controller : controllers) {
			mappingRegistry.register(controller);
		}
		logger.info("resource mapping tree\r\n{}", mappingRegistry);
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String command = httpServletRequest.getMethod();
		String URI = httpServletRequest.getRequestURI();
		String accept = httpServletRequest.getHeader("Accept");
		String query = httpServletRequest.getQueryString();
		String protocol = httpServletRequest.getProtocol().split("/")[0];
		String version = httpServletRequest.getProtocol().split("/")[1];
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
			action.setURI(URI);
			action.setQuery(query);
			action.setProtocol(protocol);
			action.setVersion(version);

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

			action.setAcceptEncode(acceptEncode);
			action.setAllowEncode(allowEncode);
			
			action.setPathEncoding(pathEncoding);
			action.setQueryEncoding(queryEncoding);
			action.setHeaderEncoding(headerEncoding);

			action.execute();
		} catch (NotFoundStatusException e) {
			chain.doFilter(request, response);
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

	public void destroy() {
		Collection<Destroyable> destroyables = beanContainer.find(Destroyable.class).values();
		for (Destroyable destroyable : destroyables) {
			destroyable.destroy();
		}
	}

}
