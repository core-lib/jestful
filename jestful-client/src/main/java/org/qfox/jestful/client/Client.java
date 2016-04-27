package org.qfox.jestful.client;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.qfox.detector.DefaultResourceDetector;
import org.qfox.detector.ResourceDetector;
import org.qfox.detector.ResourceFilter;
import org.qfox.detector.ResourceFilterChain;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Resource;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.InputStreamResource;

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
public class Client implements InvocationHandler, Actor, ResourceFilter {
	private final String protocol;
	private final String host;
	private final int port;
	private final String route;
	private final ClassLoader classLoader;
	private final Map<Class<?>, Resource> resources = new HashMap<Class<?>, Resource>();
	private final BeanContainer beanContainer;
	private final Actor actor;

	private Client(String protocol, String host, int port, String route, ClassLoader classLoader, String beanContainer, String actor) {
		super();
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.route = route;
		this.classLoader = classLoader;
		try {
			ResourceDetector detector = DefaultResourceDetector.Builder.scan("/jestful").build();
			Collection<org.qfox.detector.Resource> resources = detector.detect(this);
			DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
			XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultListableBeanFactory);
			for (org.qfox.detector.Resource resource : resources) {
				reader.loadBeanDefinitions(new InputStreamResource(resource.getInputStream()));
			}
			this.beanContainer = defaultListableBeanFactory.getBean(beanContainer, BeanContainer.class);
			this.actor = defaultListableBeanFactory.getBean(actor, Actor.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean accept(org.qfox.detector.Resource resource, ResourceFilterChain chain) {
		return resource.getName().endsWith(".xml");
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Class<?> interfase = proxy.getClass().getInterfaces()[0];
		Resource resource = resources.get(interfase);
		Mapping mapping = resource.getMappings().get(method).clone();
		Action action = new Action(beanContainer, Arrays.asList(actor, this));
		action.setResource(resource);
		action.setMapping(mapping);
		Parameter[] parameters = mapping.getParameters();
		for (int i = 0; i < args.length; i++) {
			parameters[i].setValue(args[i]);
		}
		action.setParameters(parameters);
		action.setResult(mapping.getResult());
		return action.execute();
	}

	public Object react(Action action) throws Exception {
		// TODO Auto-generated method stub
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
		private int port;
		private String route;
		private ClassLoader classLoader = this.getClass().getClassLoader();
		private String beanContainer = "defaultBeanContainer";
		private String actor = "client";

		public Client build() {
			return new Client(protocol, host, port, route, classLoader, beanContainer, actor);
		}

		public Builder setEndpoint(URL endpoint) {
			if (endpoint == null) {
				throw new IllegalArgumentException("endpoint == null");
			}
			setProtocol(endpoint.getProtocol());
			setHost(endpoint.getHost());
			setPort(endpoint.getPort() != -1 ? endpoint.getPort() : 0);
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

		public Builder setPort(int port) {
			if (port < 0 || port > 65535) {
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

		public void setBeanContainer(String beanContainer) {
			if (beanContainer == null) {
				throw new IllegalArgumentException("bean container is null");
			}
			this.beanContainer = beanContainer;
		}

		public void setActor(String actor) {
			if (actor == null) {
				throw new IllegalArgumentException("actor is null");
			}
			this.actor = actor;
		}

	}

	public String getProtocol() {
		return protocol;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getRoute() {
		return route;
	}

	@Override
	public String toString() {
		return protocol + "://" + host + (port > 0 ? port : "") + (route != null ? route : "");
	}

}
