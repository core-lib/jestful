package org.qfox.jestful.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.qfox.jestful.client.exception.NoSuchSerializerException;
import org.qfox.jestful.client.exception.UnexpectedStatusException;
import org.qfox.jestful.client.exception.UnexpectedTypeException;
import org.qfox.jestful.client.scheduler.Scheduler;
import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.io.IOUtils;
import org.qfox.jestful.core.Accepts;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Body;
import org.qfox.jestful.core.Charsets;
import org.qfox.jestful.core.Encodings;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Languages;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Parameters;
import org.qfox.jestful.core.Plugin;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Resource;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Restful;
import org.qfox.jestful.core.Result;
import org.qfox.jestful.core.Status;
import org.qfox.jestful.core.formatting.RequestSerializer;
import org.qfox.jestful.core.formatting.ResponseDeserializer;
import org.qfox.jestful.core.io.RequestLazyOutputStream;
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
public class Client implements InvocationHandler, Actor, Initialable {
	private final Map<MediaType, RequestSerializer> serializers = new HashMap<MediaType, RequestSerializer>();
	private final Map<MediaType, ResponseDeserializer> deserializers = new HashMap<MediaType, ResponseDeserializer>();
	private final Map<String, Scheduler> schedulers = new HashMap<String, Scheduler>();

	private final String protocol;
	private final String host;
	private final Integer port;
	private final String route;
	private final ClassLoader classLoader;
	private final Map<Class<?>, Resource> resources;
	private final BeanContainer beanContainer;
	private final String[] configLocations;
	private final Actor[] plugins;

	private final String[] acceptCharsets;
	private final String[] acceptEncodings;
	private final String[] acceptLanguages;

	private final String[] contentCharsets;
	private final String[] contentEncodings;
	private final String[] contentLanguages;

	private final boolean acceptEncode;
	private final boolean allowEncode;

	private final String pathEncoding;
	private final String queryEncoding;
	private final String headerEncoding;

	private Client(Builder builder) {
		super();
		this.protocol = builder.protocol;
		this.host = builder.host;
		this.port = builder.port;
		this.route = builder.route;
		this.classLoader = builder.classLoader;
		this.resources = new HashMap<Class<?>, Resource>();
		this.configLocations = builder.configLocations.toArray(new String[0]);
		DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultListableBeanFactory);
		reader.setBeanClassLoader(classLoader);
		reader.loadBeanDefinitions(configLocations);
		this.beanContainer = defaultListableBeanFactory.getBean(builder.beanContainer, BeanContainer.class);
		String[] plugins = builder.plugins.toArray(new String[0]);
		this.plugins = new Actor[plugins.length];
		for (int i = 0; i < plugins.length; i++) {
			String[] segments = plugins[i].split("\\s*;\\s*");
			this.plugins[i] = defaultListableBeanFactory.getBean(segments[0], Actor.class);
			if (this.plugins[i] instanceof Plugin) {
				Map<String, String> arguments = new LinkedHashMap<String, String>();
				for (int j = 1; j < segments.length; j++) {
					String segment = segments[j];
					String[] keyvalue = segment.split("\\s*=\\s*");
					arguments.put(keyvalue[0], keyvalue.length > 1 ? keyvalue[1] : null);
				}
				Plugin plugin = (Plugin) this.plugins[i];
				plugin.config(arguments);
			}
		}
		this.acceptCharsets = builder.acceptCharsets.toArray(new String[0]);
		this.acceptEncodings = builder.acceptEncodings.toArray(new String[0]);
		this.acceptLanguages = builder.acceptLanguages.toArray(new String[0]);

		this.contentCharsets = builder.contentCharsets.toArray(new String[0]);
		this.contentEncodings = builder.contentEncodings.toArray(new String[0]);
		this.contentLanguages = builder.contentLanguages.toArray(new String[0]);

		this.acceptEncode = builder.acceptEncode;
		this.allowEncode = builder.allowEncode;

		this.pathEncoding = builder.pathEncoding;
		this.queryEncoding = builder.queryEncoding;
		this.headerEncoding = builder.headerEncoding;

		this.initialize(this.beanContainer);
	}

	public void initialize(BeanContainer beanContainer) {
		Collection<RequestSerializer> serializers = beanContainer.find(RequestSerializer.class).values();
		for (RequestSerializer serializer : serializers) {
			String contentType = serializer.getContentType();
			MediaType mediaType = MediaType.valueOf(contentType);
			this.serializers.put(mediaType, serializer);
		}

		Collection<ResponseDeserializer> deserializers = beanContainer.find(ResponseDeserializer.class).values();
		for (ResponseDeserializer deserializer : deserializers) {
			String contentType = deserializer.getContentType();
			MediaType mediaType = MediaType.valueOf(contentType);
			this.deserializers.put(mediaType, deserializer);
		}

		Map<String, Scheduler> schedulers = beanContainer.find(Scheduler.class);
		this.schedulers.putAll(schedulers);
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Class<?> interfase = proxy.getClass().getInterfaces()[0];
		Resource resource = resources.get(interfase);
		if (resource.getMappings().containsKey(method) == false) {
			if (method.getDeclaringClass() == Object.class) {
				return method.invoke(resource, args);
			} else {
				throw new UnsupportedOperationException();
			}
		}
		Mapping mapping = resource.getMappings().get(method).clone();
		Collection<Actor> actors = new ArrayList<Actor>(Arrays.asList(plugins));
		actors.add(this);
		Action action = new Action(beanContainer, actors);
		action.setResource(resource);
		action.setMapping(mapping);
		Parameters parameters = mapping.getParameters();
		parameters.arguments(args != null ? args : new Object[0]);
		action.setParameters(parameters);
		action.setResult(mapping.getResult());

		action.setRestful(mapping.getRestful());
		action.setProtocol(protocol);
		action.setHost(host);
		action.setPort(port);
		action.setRoute(route);

		action.setConsumes(mapping.getConsumes());
		action.setProduces(mapping.getProduces());

		action.setAcceptCharsets(new Charsets(acceptCharsets));
		action.setAcceptEncodings(new Encodings(acceptEncodings));
		action.setAcceptLanguages(new Languages(acceptLanguages));
		action.setContentCharsets(new Charsets(contentCharsets));
		action.setContentEncodings(new Encodings(contentEncodings));
		action.setContentLanguages(new Languages(contentLanguages));

		action.setAcceptEncode(acceptEncode);
		action.setAllowEncode(allowEncode);

		action.setPathEncoding(pathEncoding);
		action.setQueryEncoding(queryEncoding);
		action.setHeaderEncoding(headerEncoding);

		Result result = action.getResult();
		Body body = result.getBody();
		for (Scheduler scheduler : schedulers.values()) {
			if (scheduler.supports(action)) {
				Type type = scheduler.getBodyType(this, action);
				body.setType(type);
				Object value = scheduler.schedule(this, action);
				result.setValue(value);
				return value;
			}
		}
		Type type = result.getType();
		body.setType(type);
		Object value = action.execute();
		result.setValue(value);
		return value;
	}

	private void serialize(Action action) throws Exception {
		Restful restful = action.getRestful();
		if (restful.isAcceptBody() == false) {
			return;
		}
		List<Parameter> bodies = action.getParameters().all(Position.BODY);
		Accepts consumes = action.getConsumes();
		if (bodies.isEmpty()) {
			return;
		} else {
			for (Entry<MediaType, RequestSerializer> entry : serializers.entrySet()) {
				MediaType mediaType = entry.getKey();
				RequestSerializer serializer = entry.getValue();
				if ((consumes.isEmpty() || consumes.contains(mediaType)) && serializer.supports(action)) {
					Request request = action.getRequest();
					OutputStream out = null;
					try {
						out = new RequestLazyOutputStream(request);
						serializer.serialize(action, out);
						return;
					} catch (Exception e) {
						throw e;
					} finally {
						IOUtils.close(out);
					}
				}
			}
		}
		throw new NoSuchSerializerException(action, null, consumes, serializers.values());
	}

	private void deserialize(Action action) throws Exception {
		Response response = action.getResponse();
		if (response.isResponseSuccess()) {
			Restful restful = action.getRestful();
			Result result = action.getResult();
			if (restful.isReturnBody() == false || result.getBody().getType() == Void.TYPE) {
				return;
			}
			String contentType = response.getResponseHeader("Content-Type");
			Accepts produces = action.getProduces();
			Accepts supports = new Accepts(deserializers.keySet());
			MediaType mediaType = MediaType.valueOf(contentType);
			if ((produces.isEmpty() || produces.contains(mediaType)) && supports.contains(mediaType)) {
				ResponseDeserializer deserializer = deserializers.get(mediaType);
				InputStream in = response.getResponseInputStream();
				deserializer.deserialize(action, mediaType, in);
				return;
			} else {
				if (produces.isEmpty() == false) {
					supports.retainAll(produces);
				}
				throw new UnexpectedTypeException(mediaType, supports);
			}
		} else {
			Status status = response.getResponseStatus();
			InputStream in = response.getResponseInputStream();
			String body = IOUtils.toString(in);
			throw new UnexpectedStatusException(status, body);
		}
	}

	public Object react(Action action) throws Exception {
		serialize(action);
		deserialize(action);
		return action.getResult().getBody().getValue();
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
		private Integer port = null;
		private String route = null;
		private ClassLoader classLoader = this.getClass().getClassLoader();
		private String beanContainer = "defaultBeanContainer";
		private List<String> plugins = new ArrayList<String>(Arrays.asList("client"));
		private List<String> configLocations = new ArrayList<String>(Arrays.asList("classpath*:/jestful/*.xml"));

		private List<String> acceptCharsets = new ArrayList<String>();
		private List<String> acceptEncodings = new ArrayList<String>();
		private List<String> acceptLanguages = new ArrayList<String>();

		private List<String> contentCharsets = new ArrayList<String>();
		private List<String> contentEncodings = new ArrayList<String>();
		private List<String> contentLanguages = new ArrayList<String>();

		private boolean acceptEncode = true;
		private boolean allowEncode = false;

		private String pathEncoding = "UTF-8";
		private String queryEncoding = "UTF-8";
		private String headerEncoding = "UTF-8";

		public Client build() {
			return new Client(this);
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

		public Builder setPlugins(String... plugins) {
			if (plugins == null || plugins.length == 0) {
				throw new IllegalArgumentException("plugins is null or empty array");
			}
			this.plugins = new ArrayList<String>(Arrays.asList(plugins));
			return this;
		}

		public Builder addPlugins(String... plugins) {
			if (plugins == null) {
				throw new IllegalArgumentException("plugins is null");
			}
			this.plugins.addAll(Arrays.asList(plugins));
			return this;
		}

		public Builder setConfigLocations(String... configLocations) {
			if (configLocations == null || configLocations.length == 0) {
				throw new IllegalArgumentException("config locations is null or empty");
			}
			this.configLocations = new ArrayList<String>(Arrays.asList(configLocations));
			return this;
		}

		public Builder addConfigLocations(String... configLocations) {
			if (configLocations == null) {
				throw new IllegalArgumentException("config locations is null");
			}
			this.configLocations.addAll(Arrays.asList(configLocations));
			return this;
		}

		public Builder setAcceptCharsets(String... acceptCharsets) {
			if (acceptCharsets == null || acceptCharsets.length == 0) {
				throw new IllegalArgumentException("accept charsets is null or empty");
			}
			this.acceptCharsets = new ArrayList<String>(Arrays.asList(acceptCharsets));
			return this;
		}

		public Builder addAcceptCharsets(String... acceptCharsets) {
			if (acceptCharsets == null) {
				throw new IllegalArgumentException("accept charsets is null");
			}
			this.acceptCharsets.addAll(Arrays.asList(acceptCharsets));
			return this;
		}

		public Builder setAcceptEncodings(String... acceptEncodings) {
			if (acceptEncodings == null || acceptEncodings.length == 0) {
				throw new IllegalArgumentException("accept encodings is null or empty");
			}
			this.acceptEncodings = new ArrayList<String>(Arrays.asList(acceptEncodings));
			return this;
		}

		public Builder addAcceptEncodings(String... acceptEncodings) {
			if (acceptEncodings == null) {
				throw new IllegalArgumentException("accept encodings is null");
			}
			this.acceptEncodings.addAll(Arrays.asList(acceptEncodings));
			return this;
		}

		public Builder setAcceptLanguages(String... acceptLanguages) {
			if (acceptLanguages == null || acceptLanguages.length == 0) {
				throw new IllegalArgumentException("accept languages is null or empty");
			}
			this.acceptLanguages = new ArrayList<String>(Arrays.asList(acceptLanguages));
			return this;
		}

		public Builder addAcceptLanguages(String... acceptLanguages) {
			if (acceptLanguages == null) {
				throw new IllegalArgumentException("accept languages is null");
			}
			this.acceptLanguages.addAll(Arrays.asList(acceptLanguages));
			return this;
		}

		public Builder setContentCharsets(String... contentCharsets) {
			if (contentCharsets == null || contentCharsets.length == 0) {
				throw new IllegalArgumentException("content charsets is null or empty");
			}
			this.contentCharsets = new ArrayList<String>(Arrays.asList(contentCharsets));
			return this;
		}

		public Builder addContentCharsets(String... contentCharsets) {
			if (contentCharsets == null) {
				throw new IllegalArgumentException("content charsets is null");
			}
			this.contentCharsets.addAll(Arrays.asList(contentCharsets));
			return this;
		}

		public Builder setContentEncodings(String... contentEncodings) {
			if (contentEncodings == null || contentEncodings.length == 0) {
				throw new IllegalArgumentException("content encodings is null or empty");
			}
			this.contentEncodings = new ArrayList<String>(Arrays.asList(contentEncodings));
			return this;
		}

		public Builder addContentEncodings(String... contentEncodings) {
			if (contentEncodings == null) {
				throw new IllegalArgumentException("content encodings is null");
			}
			this.contentEncodings.addAll(Arrays.asList(contentEncodings));
			return this;
		}

		public Builder setContentLanguages(String... contentLanguages) {
			if (contentLanguages == null || contentLanguages.length == 0) {
				throw new IllegalArgumentException("content languages is null or empty");
			}
			this.contentLanguages = new ArrayList<String>(Arrays.asList(contentLanguages));
			return this;
		}

		public Builder addContentLanguages(String... contentLanguages) {
			if (contentLanguages == null) {
				throw new IllegalArgumentException("content languages is null");
			}
			this.contentLanguages.addAll(Arrays.asList(contentLanguages));
			return this;
		}

		public Builder setAcceptEncode(boolean acceptEncode) {
			this.acceptEncode = acceptEncode;
			return this;
		}

		public Builder setAllowEncode(boolean allowEncode) {
			this.allowEncode = allowEncode;
			return this;
		}

		public Builder setPathEncoding(String pathEncoding) {
			if (Charset.isSupported(pathEncoding) == false) {
				throw new UnsupportedCharsetException(pathEncoding);
			}
			this.pathEncoding = pathEncoding;
			return this;
		}

		public Builder setQueryEncoding(String queryEncoding) {
			if (Charset.isSupported(queryEncoding) == false) {
				throw new UnsupportedCharsetException(queryEncoding);
			}
			this.queryEncoding = queryEncoding;
			return this;
		}

		public Builder setHeaderEncoding(String headerEncoding) {
			if (Charset.isSupported(headerEncoding) == false) {
				throw new UnsupportedCharsetException(headerEncoding);
			}
			this.headerEncoding = headerEncoding;
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

	public Actor[] getPlugins() {
		return plugins;
	}

	public Map<MediaType, RequestSerializer> getSerializers() {
		return serializers;
	}

	public Map<MediaType, ResponseDeserializer> getDeserializers() {
		return deserializers;
	}

	public Map<String, Scheduler> getSchedulers() {
		return schedulers;
	}

	public String[] getAcceptCharsets() {
		return acceptCharsets;
	}

	public String[] getAcceptEncodings() {
		return acceptEncodings;
	}

	public String[] getAcceptLanguages() {
		return acceptLanguages;
	}

	public String[] getContentCharsets() {
		return contentCharsets;
	}

	public String[] getContentEncodings() {
		return contentEncodings;
	}

	public String[] getContentLanguages() {
		return contentLanguages;
	}

	public boolean isAcceptEncode() {
		return acceptEncode;
	}

	public boolean isAllowEncode() {
		return allowEncode;
	}

	public String getPathEncoding() {
		return pathEncoding;
	}

	public String getQueryEncoding() {
		return queryEncoding;
	}

	public String getHeaderEncoding() {
		return headerEncoding;
	}

	@Override
	public String toString() {
		return protocol + "://" + host + (port != null ? ":" + port : "") + (route != null ? route : "");
	}

}
