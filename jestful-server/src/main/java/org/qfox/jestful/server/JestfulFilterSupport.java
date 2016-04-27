package org.qfox.jestful.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;

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
import org.qfox.jestful.core.Destroyable;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.Parameter;
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
	private Actor actor;

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
			String name = config.getInitParameter("actor");
			name = name == null || name.isEmpty() ? "jestful" : name;
			actor = beanContainer.get(name, Actor.class);
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
		String query = httpServletRequest.getQueryString();
		String protocol = httpServletRequest.getProtocol().split("/")[0];
		String version = httpServletRequest.getProtocol().split("/")[1];
		try {
			Mapping mapping = mappingRegistry.lookup(command, URI).clone();

			Action action = new Action(beanContainer, Arrays.asList(actor, this));

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
		Object controller = action.getResource().getController();
		Method method = action.getMapping().getMethod();
		Parameter[] parameters = action.getParameters();
		Object[] arguments = new Object[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			arguments[i] = parameters[i].getValue();
		}
		Object value = method.invoke(controller, arguments);
		result.setValue(value);
		return value;
	}

	public void destroy() {
		Collection<Destroyable> destroyables = beanContainer.find(Destroyable.class).values();
		for (Destroyable destroyable : destroyables) {
			destroyable.destroy();
		}
	}

}
