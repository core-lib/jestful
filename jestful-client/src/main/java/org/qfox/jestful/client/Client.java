package org.qfox.jestful.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.qfox.jestful.core.Accepts;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.Parameters;
import org.qfox.jestful.core.Resource;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

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
 * @date 2016年4月27日 下午4:59:46
 *
 * @since 1.0.0
 */
public class Client implements InvocationHandler, Actor {
	private final String protocol;
	private final String host;
	private final Integer port;
	private final String route;
	private final ClassLoader classLoader;
	private final Map<Class<?>, Resource> resources;
	private final String[] configLocations;
	private final BeanContainer beanContainer;
	private final Actor actor;

	private Client(String protocol, String host, Integer port, String route, ClassLoader classLoader, String[] configLocations, String beanContainer, String actor) {
		super();
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.route = route;
		this.classLoader = classLoader;
		this.resources = new HashMap<Class<?>, Resource>();
		this.configLocations = configLocations;
		DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultListableBeanFactory);
		reader.setBeanClassLoader(classLoader);
		reader.loadBeanDefinitions(configLocations);
		this.beanContainer = defaultListableBeanFactory.getBean(beanContainer, BeanContainer.class);
		this.actor = defaultListableBeanFactory.getBean(actor, Actor.class);
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Class<?> interfase = proxy.getClass().getInterfaces()[0];
		Resource resource = resources.get(interfase);
		Mapping mapping = resource.getMappings().get(method).clone();
		Action action = new Action(beanContainer, Arrays.asList(actor, this));
		action.setResource(resource);
		action.setMapping(mapping);
		Parameters parameters = new Parameters(mapping.getParameters());
		parameters.arguments(args != null ? args : new Object[0]);
		action.setParameters(parameters);
		action.setResult(mapping.getResult());

		action.setRestful(mapping.getRestful());
		action.setProtocol(protocol);
		action.setHost(host);
		action.setPort(port);
		action.setRoute(route);

		action.setConsumes(new Accepts(mapping.getConsumes()));
		action.setProduces(new Accepts(mapping.getProduces()));

		return action.execute();
	}

	public Object react(Action action) throws Exception {
		return null;
	}

	public <T> T create(Class<T> interfase) {
		Object proxy = Proxy.newProxyInstance(classLoader, new Class<?>[] { interfase }, this);
		Resource resource = new Resource(proxy, interfase);
		resources.put(interfase, resource);
		return interfase.cast(proxy);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String protocol = "http";
		private String host = "localhost";
		private Integer port;
		private String route;
		private ClassLoader classLoader = this.getClass().getClassLoader();
		private String beanContainer = "defaultBeanContainer";
		private String actor = "client";
		private String[] configLocations = new String[] { "classpath*:/jestful/*.xml" };

		public Client build() {
			return new Client(protocol, host, port, route, classLoader, configLocations, beanContainer, actor);
		}

		public Builder setEndpoint(URL endpoint) {
			if (endpoint == null) {
				throw new IllegalArgumentException("endpoint == null");
			}
			setProtocol(endpoint.getProtocol());
			setHost(endpoint.getHost());
			setPort(endpoint.getPort() < 0 ? null : endpoint.getPort());
			setRoute(endpoint.getFile());
			return this;
		}

		public Builder setProtocol(String protocol) {
			if (protocol == null) {
				throw new IllegalArgumentException("protocol == null");
			}
			this.protocol = protocol;
			return this;
		}

		public Builder setHost(String host) {
			if (protocol == null) {
				throw new IllegalArgumentException("protocol == null");
			}
			this.host = host;
			return this;
		}

		public Builder setPort(Integer port) {
			if (port != null && (port < 0 || port > 65535)) {
				throw new IllegalArgumentException("port " + port + " out of bounds [0, 65535]");
			}
			this.port = port;
			return this;
		}

		public Builder setRoute(String route) {
			if (route != null && route.isEmpty() == false && route.startsWith("/") == false) {
				throw new IllegalArgumentException("route should starts with /");
			}
			this.route = route;
			return this;
		}

		public Builder setClassLoader(ClassLoader classLoader) {
			if (classLoader == null) {
				throw new IllegalArgumentException("class loader is null");
			}
			this.classLoader = classLoader;
			return this;
		}

		public Builder setBeanContainer(String beanContainer) {
			if (beanContainer == null) {
				throw new IllegalArgumentException("bean container is null");
			}
			this.beanContainer = beanContainer;
			return this;
		}

		public Builder setActor(String actor) {
			if (actor == null) {
				throw new IllegalArgumentException("actor is null");
			}
			this.actor = actor;
			return this;
		}

		public Builder setConfigLocations(String[] configLocations) {
			if (configLocations == null || configLocations.length == 0) {
				throw new IllegalArgumentException("config locations is null or empty");
			}
			this.configLocations = configLocations;
			return this;
		}

	}

	public String getProtocol() {
		return protocol;
	}

	public String getHost() {
		return host;
	}

	public Integer getPort() {
		return port;
	}

	public String getRoute() {
		return route;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public Map<Class<?>, Resource> getResources() {
		return resources;
	}

	public String[] getConfigLocations() {
		return configLocations;
	}

	public BeanContainer getBeanContainer() {
		return beanContainer;
	}

	public Actor getActor() {
		return actor;
	}

	@Override
	public String toString() {
		return protocol + "://" + host + (port != null ? ":" + port : "") + (route != null ? route : "");
	}

}
