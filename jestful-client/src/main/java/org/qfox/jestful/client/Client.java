package org.qfox.jestful.client;

import io.loadkit.Loaders;
import org.qfox.jestful.client.catcher.Catcher;
import org.qfox.jestful.client.connection.Connection;
import org.qfox.jestful.client.connection.Connector;
import org.qfox.jestful.client.exception.NoSuchSerializerException;
import org.qfox.jestful.client.exception.UnexpectedStatusException;
import org.qfox.jestful.client.exception.UnexpectedTypeException;
import org.qfox.jestful.client.exception.UnsupportedProtocolException;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.client.scheduler.Scheduler;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.commons.conversion.ConversionProvider;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.NoSuchCharsetException;
import org.qfox.jestful.core.exception.StatusException;
import org.qfox.jestful.core.formatting.RequestSerializer;
import org.qfox.jestful.core.formatting.ResponseDeserializer;
import org.qfox.jestful.core.io.RequestLazyOutputStream;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.UrlResource;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月27日 下午4:59:46
 * @since 1.0.0
 */
public class Client implements Actor, Connector, Executor, Initialable, Destroyable {
    protected static Client defaultClient;
    protected final ExecutorService executor = Executors.newCachedThreadPool();
    protected final Charsets charsets = new Charsets(Charset.availableCharsets().keySet().toArray(new String[0]));
    protected final Map<MediaType, RequestSerializer> serializers = new LinkedHashMap<MediaType, RequestSerializer>();
    protected final Map<MediaType, ResponseDeserializer> deserializers = new LinkedHashMap<MediaType, ResponseDeserializer>();
    protected final Map<String, Scheduler> schedulers = new LinkedHashMap<String, Scheduler>();
    protected final Map<String, Connector> connectors = new LinkedHashMap<String, Connector>();
    protected final Map<String, Catcher> catchers = new LinkedHashMap<String, Catcher>();
    protected final String protocol;
    protected final String hostname;
    protected final Integer port;
    protected final String route;
    protected final ClassLoader classLoader;
    protected final Map<Class<?>, Resource> resources;
    protected final BeanContainer beanContainer;
    protected final URL[] configLocations;
    protected final Actor[] plugins;
    protected final String[] acceptCharsets;
    protected final String[] acceptEncodings;
    protected final String[] acceptLanguages;
    protected final String[] contentCharsets;
    protected final String[] contentEncodings;
    protected final String[] contentLanguages;
    protected final boolean allowEncode;
    protected final boolean acceptEncode;
    protected final String pathEncodeCharset;
    protected final String queryEncodeCharset;
    protected final String headerEncodeCharset;
    protected final int connTimeout;
    protected final int readTimeout;
    protected final int writeTimeout;
    protected final Gateway gateway;
    protected final Boolean keepAlive;
    protected final Integer idleTimeout;
    protected final HostnameVerifier hostnameVerifier;
    protected final SSLSocketFactory SSLSocketFactory;
    protected final String userAgent;
    protected final boolean configValidated;
    protected final DateFormat serializationDateFormat;
    protected final DateFormat deserializationDateFormat;
    protected boolean destroyed = false;

    protected Client(Builder<?> builder) throws IOException {
        super();
        this.protocol = builder.protocol;
        this.hostname = builder.hostname;
        this.port = builder.port;
        this.route = builder.route;
        this.classLoader = builder.classLoader;
        this.resources = new LinkedHashMap<Class<?>, Resource>();
        this.configLocations = integrate(classLoader).toArray(new URL[0]);
        this.configValidated = builder.configValidating;
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultListableBeanFactory);
        reader.setValidating(configValidated);
        reader.setBeanClassLoader(classLoader);
        for (URL url : configLocations) reader.loadBeanDefinitions(new UrlResource(url));
        this.beanContainer = defaultListableBeanFactory.getBean(builder.beanContainer, BeanContainer.class);
        this.plugins = load(builder.plugins);

        this.acceptCharsets = builder.acceptCharsets.toArray(new String[0]);
        this.acceptEncodings = builder.acceptEncodings.toArray(new String[0]);
        this.acceptLanguages = builder.acceptLanguages.toArray(new String[0]);

        this.contentCharsets = builder.contentCharsets.toArray(new String[0]);
        this.contentEncodings = builder.contentEncodings.toArray(new String[0]);
        this.contentLanguages = builder.contentLanguages.toArray(new String[0]);

        this.acceptEncode = builder.acceptEncode;
        this.allowEncode = builder.allowEncode;

        this.pathEncodeCharset = builder.pathEncodeCharset;
        this.queryEncodeCharset = builder.queryEncodeCharset;
        this.headerEncodeCharset = builder.headerEncodeCharset;

        this.connTimeout = builder.connTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;

        this.gateway = builder.gateway;
        this.keepAlive = builder.keepAlive;
        this.idleTimeout = builder.idleTimeout;

        this.hostnameVerifier = builder.hostnameVerifier;
        this.SSLSocketFactory = builder.SSLSocketFactory;
        this.userAgent = builder.userAgent;
        this.serializationDateFormat = builder.serializationDateFormat;
        this.deserializationDateFormat = builder.deserializationDateFormat;

        this.initialize(this.beanContainer);
    }

    public static Client getDefaultClient() {
        if (defaultClient != null) {
            return defaultClient;
        }
        synchronized (Client.class) {
            if (defaultClient != null) {
                return defaultClient;
            }
            return defaultClient = builder().build();
        }
    }

    public static Builder<?> builder() {
        return new Builder();
    }

    protected Set<URL> integrate(ClassLoader classLoader) throws IOException {
        Set<URL> urls = new LinkedHashSet<URL>();
        Enumeration<io.loadkit.Resource> resources = Loaders.ant(classLoader).load("jestful/**.xml");
        while (resources.hasMoreElements()) {
            io.loadkit.Resource resource = resources.nextElement();
            urls.add(resource.getUrl());
        }
        return urls;
    }

    protected Actor[] load(List<String> plugins) {
        return load(plugins, Actor.class);
    }

    protected <T extends Actor> T[] load(List<String> plugins, Class<T> pluginType) {
        T[] actors = (T[]) Array.newInstance(pluginType, plugins.size());
        for (int i = 0; i < plugins.size(); i++) {
            String[] segments = plugins.get(i).split("\\s*;\\s*");
            String name = segments[0];
            Actor actor = beanContainer.get(name, Actor.class);

            if (!pluginType.isInstance(actor)) throw new ClassCastException("bean [" + name + "] is a " + pluginType.getSimpleName());

            if (actor instanceof Configurable) {
                Map<String, String> arguments = new LinkedHashMap<String, String>();
                for (int j = 1; j < segments.length; j++) {
                    String segment = segments[j];
                    String[] keyvalue = segment.split("\\s*=\\s*");
                    arguments.put(keyvalue[0], keyvalue.length > 1 ? keyvalue[1] : null);
                }
                Configurable configurable = (Plugin) actor;
                configurable.config(arguments);
            }
            actors[i] = (T) actor;
        }
        return actors;
    }

    public void initialize(BeanContainer beanContainer) {
        Collection<ConversionProvider> providers = beanContainer.find(ConversionProvider.class).values();
        for (ConversionProvider provider : providers) {
            provider.setSerializationDateFormat(serializationDateFormat);
            provider.setDeserializationDateFormat(deserializationDateFormat);
        }

        Collection<RequestSerializer> serializers = beanContainer.find(RequestSerializer.class).values();
        for (RequestSerializer serializer : serializers) {
            String contentType = serializer.getContentType();
            MediaType mediaType = MediaType.valueOf(contentType);
            this.serializers.put(mediaType, serializer);
            serializer.setSerializationDateFormat(serializationDateFormat);
        }

        Collection<ResponseDeserializer> deserializers = beanContainer.find(ResponseDeserializer.class).values();
        for (ResponseDeserializer deserializer : deserializers) {
            String contentType = deserializer.getContentType();
            MediaType mediaType = MediaType.valueOf(contentType);
            this.deserializers.put(mediaType, deserializer);
            deserializer.setDeserializationDateFormat(deserializationDateFormat);
        }

        Map<String, Connector> connectors = beanContainer.find(Connector.class);
        this.connectors.putAll(connectors);

        Map<String, Scheduler> schedulers = beanContainer.find(Scheduler.class);
        this.schedulers.putAll(schedulers);

        Map<String, Catcher> catchers = beanContainer.find(Catcher.class);
        this.catchers.putAll(catchers);
    }

    @Override
    public synchronized void destroy() {
        if (destroyed) return;
        destroyed = true;
        if (beanContainer != null) beanContainer.destroy();
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    protected void finalize() throws Throwable {
        this.destroy();
        super.finalize();
    }

    @Override
    public void execute(Runnable command) {
        executor.execute(command);
    }

    public boolean supports(Action action) {
        for (Connector connector : connectors.values()) {
            if (connector.supports(action)) {
                return true;
            }
        }
        return false;
    }

    public Connection connect(Action action, Gateway gateway, Client client) throws IOException {
        Connection connection = (Connection) action.getExtra().get(Connection.class);
        if (connection != null) {
            return connection;
        }
        for (Connector connector : connectors.values()) {
            if (connector.supports(action)) {
                connection = connector.connect(action, gateway, this);
                action.getExtra().put(Connection.class, connection);
                return connection;
            }
        }
        throw new UnsupportedProtocolException(action.getProtocol());
    }

    public SocketAddress address(Action action, Gateway gateway, Client client) throws IOException {
        Connection connection = (Connection) action.getExtra().get(Connection.class);
        if (connection != null) {
            return connection.getAddress();
        }
        for (Connector connector : connectors.values()) {
            if (connector.supports(action)) {
                return connector.address(action, gateway, this);
            }
        }
        throw new UnsupportedProtocolException(action.getProtocol());
    }

    public void serialize(Action action) throws Exception {
        Request request = action.getRequest();
        List<Parameter> bodies = action.getParameters().all(Position.BODY);
        if (bodies.isEmpty()) {
            request.connect();
        } else {
            String charset;
            Charsets options = action.getContentCharsets().clone();
            if (options.isEmpty()) {
                charset = Charset.defaultCharset().name();
            } else {
                options.retainAll(charsets);
                if (options.isEmpty()) throw new NoSuchCharsetException(action.getContentCharsets().clone(), charsets.clone());
                charset = options.first().getName();
            }
            Accepts consumes = action.getConsumes();
            for (Entry<MediaType, RequestSerializer> entry : serializers.entrySet()) {
                MediaType mediaType = entry.getKey();
                RequestSerializer serializer = entry.getValue();
                if ((consumes.isEmpty() || consumes.contains(mediaType)) && serializer.supports(action)) {
                    OutputStream out = null;
                    try {
                        out = new RequestLazyOutputStream(request);
                        serializer.serialize(action, charset, out);
                        out.flush();
                        return;
                    } catch (Exception e) {
                        throw e;
                    } finally {
                        IOKit.close(out);
                    }
                }
            }
            throw new NoSuchSerializerException(action, null, consumes, serializers.values());
        }
    }

    public void deserialize(Action action) throws Exception {
        Response response = action.getResponse();
        Body body = action.getResult().getBody();
        if (body.getType() == Void.TYPE) return;
        String contentType = response.getContentType();
        Accepts produces = action.getProduces();
        Accepts supports = new Accepts(deserializers.keySet());
        MediaType mediaType = MediaType.valueOf(contentType);
        if (body.getType() == Message.class) {
            Message message = new Message(response);
            body.setValue(message);
        } else if (body.getType() == Entity.class) {
            Entity entity = new Entity(response);
            body.setValue(entity);
        } else if (body.getType() == Header.class) {
            Header header = new Header(response);
            body.setValue(header);
        } else if ((produces.isEmpty() || produces.contains(mediaType)) && supports.contains(mediaType)) {
            String charset = mediaType.getCharset();
            if (StringKit.isBlank(charset)) charset = response.getResponseHeader("Content-Charset");
            if (StringKit.isBlank(charset)) charset = response.getCharacterEncoding();
            if (StringKit.isBlank(charset)) charset = Charset.defaultCharset().name();
            ResponseDeserializer deserializer = deserializers.get(mediaType);
            InputStream in = response.getResponseInputStream();
            deserializer.deserialize(action, mediaType, charset, in);
        } else if (body.getType() == String.class) {
            String charset = mediaType.getCharset();
            if (StringKit.isBlank(charset)) charset = response.getResponseHeader("Content-Charset");
            if (StringKit.isBlank(charset)) charset = response.getCharacterEncoding();
            if (StringKit.isBlank(charset)) charset = Charset.defaultCharset().name();
            InputStream in = response.getResponseInputStream();
            InputStreamReader reader = new InputStreamReader(in, charset);
            String value = IOKit.toString(reader);
            body.setValue(value);
        } else if (body.getType() == File.class) {
            InputStream in = response.getResponseInputStream();
            File file = File.createTempFile("jestful", ".tmp");
            IOKit.transfer(in, file);
            body.setValue(file);
        } else if (produces.size() == 1) {
            String charset = mediaType.getCharset();
            mediaType = produces.iterator().next();
            if (StringKit.isBlank(charset)) charset = response.getResponseHeader("Content-Charset");
            if (StringKit.isBlank(charset)) charset = response.getCharacterEncoding();
            if (StringKit.isBlank(charset)) charset = mediaType.getCharset();
            if (StringKit.isBlank(charset)) charset = Charset.defaultCharset().name();
            ResponseDeserializer deserializer = deserializers.get(mediaType);
            InputStream in = response.getResponseInputStream();
            deserializer.deserialize(action, mediaType, charset, in);
        } else {
            if (!produces.isEmpty()) supports.retainAll(produces);
            throw new UnexpectedTypeException(mediaType, supports);
        }
    }

    protected Object call(Action action) throws Exception {
        Request request = action.getRequest();
        Response response = action.getResponse();
        try {
            Restful restful = action.getRestful();

            if (restful.isAcceptBody()) serialize(action);
            else request.connect();

            validate(action);

            if (restful.isReturnBody()) {
                deserialize(action);
                return action.getResult().getBody().getValue();
            } else {
                Map<String, String> header = new CaseInsensitiveMap<String, String>();
                for (String key : response.getHeaderKeys()) header.put(key != null ? key : "", response.getResponseHeader(key));
                return header;
            }
        } catch (StatusException se) {
            for (Catcher catcher : catchers.values()) if (catcher.catchable(se)) return catcher.caught(Client.this, action, se);
            throw se;
        } finally {
            IOKit.close(request);
            IOKit.close(response);
        }
    }

    private void validate(Action action) throws IOException {
        Response response = action.getResponse();
        if (!response.isResponseSuccess()) {
            String contentType = response.getContentType();
            MediaType mediaType = MediaType.valueOf(contentType);
            String charset = mediaType.getCharset();
            if (StringKit.isBlank(charset)) charset = response.getResponseHeader("Content-Charset");
            if (StringKit.isBlank(charset)) charset = response.getCharacterEncoding();
            if (StringKit.isBlank(charset)) charset = Charset.defaultCharset().name();
            Status status = response.getResponseStatus();
            Map<String, String[]> header = new CaseInsensitiveMap<String, String[]>();
            String[] keys = response.getHeaderKeys();
            for (String key : keys) header.put(key == null ? "" : key, response.getResponseHeaders(key));
            InputStream in = response.getResponseInputStream();
            InputStreamReader reader = in == null ? null : new InputStreamReader(in, charset);
            String body = reader != null ? IOKit.toString(reader) : "";
            throw new UnexpectedStatusException(action.getRequestURI(), action.getRestful().getMethod(), status, header, body);
        }
    }

    public Object react(Action action) throws Exception {
        return new BioPromise(action);
    }

    protected Object schedule(Action action) throws Exception {
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

        try {
            Type type = result.getType();
            body.setType(type);
            Promise promise = (Promise) action.execute();
            Object value = promise.acquire();
            result.setValue(value);
            return value;
        } catch (Exception e) {
            if (body.getType() != Message.class) throw e;
            Response response = action.getResponse();
            Message message = new Message(response, e);
            body.setValue(message);
            return message;
        }
    }

    protected Request newRequest(Action action) throws Exception {
        Request request = new JestfulClientRequest(action, this, gateway, connTimeout, readTimeout, writeTimeout);
        request.setRequestHeader("User-Agent", userAgent);
        return request;
    }

    protected Response newResponse(Action action) throws Exception {
        return new JestfulClientResponse(action, this, gateway);
    }

    public Invoker<?> invoker() {
        return new Invoker();
    }

    public <T> T create(Class<T> interfase) {
        return creator().create(interfase);
    }

    public <T> T create(Class<T> interfase, String protocol, String host) {
        return creator().create(interfase, protocol, host);
    }

    public <T> T create(Class<T> interfase, String protocol, String host, Integer port) {
        return creator().create(interfase, protocol, host, port);
    }

    public <T> T create(Class<T> interfase, String protocol, String host, Integer port, String route) {
        return creator().create(interfase, protocol, host, port, route);
    }

    public <T> T create(Class<T> interfase, String endpoint) {
        return creator().create(interfase, endpoint);
    }

    public <T> T create(Class<T> interfase, URL endpoint) {
        return creator().create(interfase, endpoint);
    }

    public Creator<?> creator() {
        return new Creator();
    }

    public Charsets getCharsets() {
        return charsets;
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

    public Map<String, Connector> getConnectors() {
        return connectors;
    }

    public Map<String, Catcher> getCatchers() {
        return catchers;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHostname() {
        return hostname;
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

    public BeanContainer getBeanContainer() {
        return beanContainer;
    }

    public URL[] getConfigLocations() {
        return configLocations;
    }

    public Actor[] getPlugins() {
        return plugins;
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

    public boolean isAllowEncode() {
        return allowEncode;
    }

    public boolean isAcceptEncode() {
        return acceptEncode;
    }

    public String getPathEncodeCharset() {
        return pathEncodeCharset;
    }

    public String getQueryEncodeCharset() {
        return queryEncodeCharset;
    }

    public String getHeaderEncodeCharset() {
        return headerEncodeCharset;
    }

    public int getConnTimeout() {
        return connTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public Integer getIdleTimeout() {
        return idleTimeout;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public SSLSocketFactory getSSLSocketFactory() {
        return SSLSocketFactory;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public boolean isConfigValidated() {
        return configValidated;
    }

    public DateFormat getSerializationDateFormat() {
        return serializationDateFormat;
    }

    public DateFormat getDeserializationDateFormat() {
        return deserializationDateFormat;
    }

    @Override
    public String toString() {
        return protocol + "://" + hostname + (port != null ? ":" + port : "") + (route != null ? route : "");
    }

    public static class Builder<B extends Builder<B>> {
        protected String protocol = "http";
        protected String hostname = "localhost";
        protected Integer port = null;
        protected String route = null;
        protected ClassLoader classLoader = this.getClass().getClassLoader();
        protected String beanContainer = "defaultBeanContainer";
        protected List<String> plugins = new ArrayList<String>(Collections.singletonList("client"));

        protected List<String> acceptCharsets = new ArrayList<String>();
        protected List<String> acceptEncodings = new ArrayList<String>();
        protected List<String> acceptLanguages = new ArrayList<String>();

        protected List<String> contentCharsets = new ArrayList<String>();
        protected List<String> contentEncodings = new ArrayList<String>();
        protected List<String> contentLanguages = new ArrayList<String>();

        protected boolean allowEncode = false;
        protected boolean acceptEncode = true;

        protected String pathEncodeCharset = "UTF-8";
        protected String queryEncodeCharset = "UTF-8";
        protected String headerEncodeCharset = "UTF-8";

        protected int connTimeout = 0;
        protected int readTimeout = 0;
        protected int writeTimeout = 0;

        protected Gateway gateway = Gateway.NULL;
        protected Boolean keepAlive = null; // 用通讯协议的缺省方案
        protected Integer idleTimeout = null;

        protected HostnameVerifier hostnameVerifier;
        protected SSLSocketFactory SSLSocketFactory;

        protected String userAgent = null;

        protected boolean configValidating = false;

        protected DateFormat serializationDateFormat = DateFormat.getDateTimeInstance();
        protected DateFormat deserializationDateFormat = DateFormat.getDateTimeInstance();

        protected Builder() {
            this.userAgent = "Mozilla/5.0"
                    + " "
                    + "("
                    + System.getProperty("os.name")
                    + " "
                    + System.getProperty("os.version")
                    + "; "
                    + System.getProperty("os.arch")
                    + "; "
                    + System.getProperty("user.language")
                    + ")"
                    + " "
                    + Module.getInstance().getParentName()
                    + "/"
                    + Module.getInstance().getParentVersion()
                    + " "
                    + Module.getInstance().getName()
                    + "/"
                    + Module.getInstance().getVersion();
        }

        public Client build() {
            try {
                return new Client(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public B setEndpoint(URL endpoint) {
            if (endpoint == null) {
                throw new IllegalArgumentException("endpoint == null");
            }
            setProtocol(endpoint.getProtocol());
            setHostname(endpoint.getHost());
            setPort(endpoint.getPort() < 0 ? null : endpoint.getPort());
            setRoute(endpoint.getFile().length() == 0 ? null : endpoint.getFile());
            return (B) this;
        }

        public B setProtocol(String protocol) {
            if (protocol == null) {
                throw new IllegalArgumentException("protocol == null");
            }
            this.protocol = protocol;
            return (B) this;
        }

        public B setHostname(String hostname) {
            if (hostname == null) {
                throw new IllegalArgumentException("hostname == null");
            }
            this.hostname = hostname;
            return (B) this;
        }

        public B setPort(Integer port) {
            if (port != null && (port < 0 || port > 65535)) {
                throw new IllegalArgumentException("port " + port + " out of bounds [0, 65535]");
            }
            this.port = port;
            return (B) this;
        }

        public B setRoute(String route) {
            if (route != null && route.length() == 0 == false && !route.startsWith("/")) {
                throw new IllegalArgumentException("route should starts with /");
            }
            this.route = route;
            return (B) this;
        }

        public B setClassLoader(ClassLoader classLoader) {
            if (classLoader == null) {
                throw new IllegalArgumentException("class loader is null");
            }
            this.classLoader = classLoader;
            return (B) this;
        }

        public B setBeanContainer(String beanContainer) {
            if (beanContainer == null) {
                throw new IllegalArgumentException("bean container is null");
            }
            this.beanContainer = beanContainer;
            return (B) this;
        }

        public B setPlugins(String... plugins) {
            if (plugins == null || plugins.length == 0) {
                throw new IllegalArgumentException("plugins is null or empty array");
            }
            this.plugins = new ArrayList<String>(Arrays.asList(plugins));
            return (B) this;
        }

        public B addPlugins(String... plugins) {
            if (plugins == null) {
                throw new IllegalArgumentException("plugins is null");
            }
            this.plugins.addAll(Arrays.asList(plugins));
            return (B) this;
        }

        public B setAcceptCharsets(String... acceptCharsets) {
            if (acceptCharsets == null || acceptCharsets.length == 0) {
                throw new IllegalArgumentException("accept charsets is null or empty");
            }
            this.acceptCharsets = new ArrayList<String>(Arrays.asList(acceptCharsets));
            return (B) this;
        }

        public B addAcceptCharsets(String... acceptCharsets) {
            if (acceptCharsets == null) {
                throw new IllegalArgumentException("accept charsets is null");
            }
            this.acceptCharsets.addAll(Arrays.asList(acceptCharsets));
            return (B) this;
        }

        public B setAcceptEncodings(String... acceptEncodings) {
            if (acceptEncodings == null || acceptEncodings.length == 0) {
                throw new IllegalArgumentException("accept encodings is null or empty");
            }
            this.acceptEncodings = new ArrayList<String>(Arrays.asList(acceptEncodings));
            return (B) this;
        }

        public B addAcceptEncodings(String... acceptEncodings) {
            if (acceptEncodings == null) {
                throw new IllegalArgumentException("accept encodings is null");
            }
            this.acceptEncodings.addAll(Arrays.asList(acceptEncodings));
            return (B) this;
        }

        public B setAcceptLanguages(String... acceptLanguages) {
            if (acceptLanguages == null || acceptLanguages.length == 0) {
                throw new IllegalArgumentException("accept languages is null or empty");
            }
            this.acceptLanguages = new ArrayList<String>(Arrays.asList(acceptLanguages));
            return (B) this;
        }

        public B addAcceptLanguages(String... acceptLanguages) {
            if (acceptLanguages == null) {
                throw new IllegalArgumentException("accept languages is null");
            }
            this.acceptLanguages.addAll(Arrays.asList(acceptLanguages));
            return (B) this;
        }

        public B setContentCharsets(String... contentCharsets) {
            if (contentCharsets == null || contentCharsets.length == 0) {
                throw new IllegalArgumentException("content charsets is null or empty");
            }
            this.contentCharsets = new ArrayList<String>(Arrays.asList(contentCharsets));
            return (B) this;
        }

        public B addContentCharsets(String... contentCharsets) {
            if (contentCharsets == null) {
                throw new IllegalArgumentException("content charsets is null");
            }
            this.contentCharsets.addAll(Arrays.asList(contentCharsets));
            return (B) this;
        }

        public B setContentEncodings(String... contentEncodings) {
            if (contentEncodings == null || contentEncodings.length == 0) {
                throw new IllegalArgumentException("content encodings is null or empty");
            }
            this.contentEncodings = new ArrayList<String>(Arrays.asList(contentEncodings));
            return (B) this;
        }

        public B addContentEncodings(String... contentEncodings) {
            if (contentEncodings == null) {
                throw new IllegalArgumentException("content encodings is null");
            }
            this.contentEncodings.addAll(Arrays.asList(contentEncodings));
            return (B) this;
        }

        public B setContentLanguages(String... contentLanguages) {
            if (contentLanguages == null || contentLanguages.length == 0) {
                throw new IllegalArgumentException("content languages is null or empty");
            }
            this.contentLanguages = new ArrayList<String>(Arrays.asList(contentLanguages));
            return (B) this;
        }

        public B addContentLanguages(String... contentLanguages) {
            if (contentLanguages == null) {
                throw new IllegalArgumentException("content languages is null");
            }
            this.contentLanguages.addAll(Arrays.asList(contentLanguages));
            return (B) this;
        }

        public B setAllowEncode(boolean allowEncode) {
            this.allowEncode = allowEncode;
            return (B) this;
        }

        public B setAcceptEncode(boolean acceptEncode) {
            this.acceptEncode = acceptEncode;
            return (B) this;
        }

        public B setPathEncodeCharset(String pathEncodeCharset) {
            if (!Charset.isSupported(pathEncodeCharset)) {
                throw new UnsupportedCharsetException(pathEncodeCharset);
            }
            this.pathEncodeCharset = pathEncodeCharset;
            return (B) this;
        }

        public B setQueryEncodeCharset(String queryEncodeCharset) {
            if (!Charset.isSupported(queryEncodeCharset)) {
                throw new UnsupportedCharsetException(queryEncodeCharset);
            }
            this.queryEncodeCharset = queryEncodeCharset;
            return (B) this;
        }

        public B setHeaderEncodeCharset(String headerEncodeCharset) {
            if (!Charset.isSupported(headerEncodeCharset)) {
                throw new UnsupportedCharsetException(headerEncodeCharset);
            }
            this.headerEncodeCharset = headerEncodeCharset;
            return (B) this;
        }

        public B setConnTimeout(int connTimeout) {
            if (connTimeout < 0) {
                throw new IllegalArgumentException("connect timeout is negative");
            }
            this.connTimeout = connTimeout;
            return (B) this;
        }

        public B setReadTimeout(int readTimeout) {
            if (readTimeout < 0) {
                throw new IllegalArgumentException("reading timeout is negative");
            }
            this.readTimeout = readTimeout;
            return (B) this;
        }

        public B setWriteTimeout(int writeTimeout) {
            if (writeTimeout < 0) {
                throw new IllegalArgumentException("writing timeout is negative");
            }
            this.writeTimeout = writeTimeout;
            return (B) this;
        }

        public B setGateway(Gateway gateway) {
            if (gateway == null) {
                throw new IllegalArgumentException("can not set null gateway");
            }
            this.gateway = gateway;
            return (B) this;
        }

        public B setKeepAlive(Boolean keepAlive) {
            this.keepAlive = keepAlive;
            return (B) this;
        }

        public B setIdleTimeout(Integer idleTimeout) {
            this.idleTimeout = idleTimeout;
            return (B) this;
        }

        public B setHostnameVerifier(HostnameVerifier hostnameVerifier) {
            if (hostnameVerifier == null) {
                throw new IllegalArgumentException("can not set null hostnameVerifier");
            }
            this.hostnameVerifier = hostnameVerifier;
            return (B) this;
        }

        public B setSSLSocketFactory(SSLSocketFactory SSLSocketFactory) {
            if (SSLSocketFactory == null) {
                throw new IllegalArgumentException("can not set null SSLSocketFactory");
            }
            this.SSLSocketFactory = SSLSocketFactory;
            return (B) this;
        }

        public B setUserAgent(String userAgent) {
            if (userAgent == null) {
                throw new IllegalArgumentException("User-Agent can not be null");
            }
            this.userAgent = userAgent;
            return (B) this;
        }

        public B setConfigValidating(boolean configValidating) {
            this.configValidating = configValidating;
            return (B) this;
        }

        public B setSerializationDatePattern(String datePattern) {
            if (datePattern == null) {
                throw new IllegalArgumentException("can not set null datePattern");
            }
            return setSerializationDateFormat(new SimpleDateFormat(datePattern));
        }

        public B setSerializationDateFormat(DateFormat dateFormat) {
            if (dateFormat == null) {
                throw new IllegalArgumentException("can not set null dateFormat");
            }
            this.serializationDateFormat = dateFormat;
            return (B) this;
        }

        public B setDeserializationDatePattern(String datePattern) {
            if (datePattern == null) {
                throw new IllegalArgumentException("can not set null datePattern");
            }
            return setDeserializationDateFormat(new SimpleDateFormat(datePattern));
        }

        public B setDeserializationDateFormat(DateFormat dateFormat) {
            if (dateFormat == null) {
                throw new IllegalArgumentException("can not set null dateFormat");
            }
            this.deserializationDateFormat = dateFormat;
            return (B) this;
        }

        public B setDatePattern(String datePattern) {
            if (datePattern == null) {
                throw new IllegalArgumentException("can not set null datePattern");
            }
            return setDateFormat(new SimpleDateFormat(datePattern));
        }

        public B setDateFormat(DateFormat dateFormat) {
            setSerializationDateFormat(dateFormat);
            setDeserializationDateFormat(dateFormat);
            return (B) this;
        }
    }

    protected class BioPromise implements Promise {
        protected final Object lock = new Object();

        protected final Action action;
        protected volatile Boolean success;
        protected volatile Object result;
        protected volatile Exception exception;
        protected volatile boolean canceled;

        protected BioPromise(Action action) {
            this.action = action;
        }

        @Override
        public Object acquire() throws Exception {
            if (success == null) {
                synchronized (lock) {
                    if (canceled) throw new IllegalStateException("canceled");
                    if (success == null) {
                        try {
                            result = call(action);
                        } catch (Exception e) {
                            exception = e;
                        } finally {
                            success = exception == null;
                        }
                    }
                    return acquire();
                }
            } else if (success) {
                return result;
            } else {
                throw exception;
            }
        }

        @Override
        public void accept(final Callback<Object> callback) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Object result = null;
                    Exception exception = null;
                    try {
                        callback.onSuccess(result = acquire());
                    } catch (Exception e) {
                        callback.onFail(exception = e);
                    } finally {
                        callback.onCompleted(exception == null, result, exception);
                    }
                }
            });
        }

        @Override
        public void cancel() {
            if (canceled) return;
            synchronized (lock) {
                if (canceled) return;
                canceled = true;
            }
        }

        @Override
        public Client client() {
            return Client.this;
        }
    }

    public class Invoker<I extends Invoker<I>> {
        private String protocol = Client.this.protocol;
        private String hostname = Client.this.hostname;
        private Integer port = Client.this.port;
        private String route = Client.this.route;
        private Resource resource;
        private Mapping mapping;
        private Restful restful;
        private Parameters parameters;
        private Result result;

        private Accepts consumes = Accepts.valueOf("");
        private Accepts produces = Accepts.valueOf("");

        private List<ForePlugin> forePlugins = new ArrayList<ForePlugin>();
        private List<BackPlugin> backPlugins = new ArrayList<BackPlugin>();

        private Map<Object, Object> extra = new LinkedHashMap<Object, Object>();

        public I setEndpoint(URL endpoint) {
            if (endpoint == null) {
                throw new IllegalArgumentException("endpoint == null");
            }
            setProtocol(endpoint.getProtocol());
            setHostname(endpoint.getHost());
            setPort(endpoint.getPort() < 0 ? null : endpoint.getPort());
            setRoute(endpoint.getFile().length() == 0 ? null : endpoint.getFile());
            return (I) this;
        }

        public I setProtocol(String protocol) {
            if (protocol == null) {
                throw new IllegalArgumentException("protocol == null");
            }
            this.protocol = protocol;
            return (I) this;
        }

        public I setHostname(String hostname) {
            if (protocol == null) {
                throw new IllegalArgumentException("hostname == null");
            }
            this.hostname = hostname;
            return (I) this;
        }

        public I setPort(Integer port) {
            if (port != null && (port < 0 || port > 65535)) {
                throw new IllegalArgumentException("port " + port + " out of bounds [0, 65535]");
            }
            this.port = port;
            return (I) this;
        }

        public I setRoute(String route) {
            if (route != null && route.length() == 0 == false && !route.startsWith("/")) {
                throw new IllegalArgumentException("route should starts with /");
            }
            this.route = route;
            return (I) this;
        }

        public I setResource(Resource resource) {
            this.resource = resource;
            return (I) this;
        }

        public I setMapping(Mapping mapping) {
            this.mapping = mapping;
            if (mapping != null) {
                this.restful = mapping.getRestful();
                this.parameters = mapping.getParameters();
                this.result = mapping.getResult();
                this.consumes = mapping.getConsumes();
                this.produces = mapping.getProduces();
            }
            return (I) this;
        }

        public I setRestful(Restful restful) {
            this.restful = restful;
            return (I) this;
        }

        public I setParameters(Parameters parameters) {
            this.parameters = parameters;
            return (I) this;
        }

        public I setResult(Result result) {
            this.result = result;
            return (I) this;
        }

        public I setConsumes(Accepts consumes) {
            this.consumes = consumes;
            return (I) this;
        }

        public I setProduces(Accepts produces) {
            this.produces = produces;
            return (I) this;
        }

        public I setForePlugins(String... plugins) {
            this.forePlugins.clear();
            return addForePlugins(plugins);
        }

        public I setBackPlugins(String... plugins) {
            this.backPlugins.clear();
            return addBackPlugins(plugins);
        }

        public I addForePlugins(String... plugins) {
            Collections.addAll(forePlugins, load(Arrays.asList(plugins), ForePlugin.class));
            return (I) this;
        }

        public I addBackPlugins(String... plugins) {
            Collections.addAll(backPlugins, load(Arrays.asList(plugins), BackPlugin.class));
            return (I) this;
        }

        public I setForePlugins(ForePlugin... plugins) {
            this.forePlugins.clear();
            return addForePlugins(plugins);
        }

        public I setBackPlugins(BackPlugin... plugins) {
            this.backPlugins.clear();
            return addBackPlugins(plugins);
        }

        public I addForePlugins(ForePlugin... plugins) {
            Collections.addAll(forePlugins, plugins);
            return (I) this;
        }

        public I addBackPlugins(BackPlugin... plugins) {
            Collections.addAll(backPlugins, plugins);
            return (I) this;
        }

        public I setForePlugins(Iterable<ForePlugin> plugins) {
            this.forePlugins.clear();
            return addForePlugins(plugins);
        }

        public I setBackPlugins(Iterable<BackPlugin> plugins) {
            this.backPlugins.clear();
            return addBackPlugins(plugins);
        }

        public I addForePlugins(Iterable<ForePlugin> plugins) {
            for (ForePlugin plugin : plugins) this.forePlugins.add(plugin);
            return (I) this;
        }

        public I addBackPlugins(Iterable<BackPlugin> plugins) {
            for (BackPlugin plugin : plugins) this.backPlugins.add(plugin);
            return (I) this;
        }

        public I setExtra(Map<Object, Object> extra) {
            this.extra.clear();
            return addExtra(extra);
        }

        public I addExtra(Map<Object, Object> extra) {
            this.extra.putAll(extra);
            return (I) this;
        }

        public I setExtra(Object key, Object value) {
            this.extra.clear();
            return addExtra(key, value);
        }

        public I addExtra(Object key, Object value) {
            this.extra.put(key, value);
            return (I) this;
        }

        public Action draft(Object... args) throws Exception {
            if (resource == null) resource = new Resource();
            if (mapping == null) mapping = new Mapping(resource, parameters, result, restful, consumes, produces);
            for (int i = 0; args != null && parameters != null && i < args.length && i < parameters.size(); i++) parameters.get(i).setValue(args[i]);

            Collection<Actor> actors = new ArrayList<Actor>();
            actors.addAll(forePlugins);
            actors.addAll(Arrays.asList(plugins));
            actors.addAll(backPlugins);
            actors.add(Client.this);

            Action action = new Action(beanContainer, actors, forePlugins, backPlugins);
            action.setResource(resource);
            action.setMapping(mapping);
            action.setParameters(parameters);
            action.setResult(result);

            action.setRestful(restful);
            action.setProtocol(protocol);
            action.setHostname(hostname);
            action.setPort(port);
            action.setRoute(route);

            action.setRequest(newRequest(action));
            action.setResponse(newResponse(action));

            action.setConsumes(consumes);
            action.setProduces(produces);

            action.setAcceptCharsets(new Charsets(acceptCharsets));
            action.setAcceptEncodings(new Encodings(acceptEncodings));
            action.setAcceptLanguages(new Languages(acceptLanguages));
            action.setContentCharsets(new Charsets(contentCharsets));
            action.setContentEncodings(new Encodings(contentEncodings));
            action.setContentLanguages(new Languages(contentLanguages));

            action.setAllowEncode(allowEncode);
            action.setAcceptEncode(acceptEncode);

            action.setPathEncodeCharset(pathEncodeCharset);
            action.setQueryEncodeCharset(queryEncodeCharset);
            action.setHeaderEncodeCharset(headerEncodeCharset);

            action.getExtra().putAll(extra);
            action.getExtra().put(Client.class, Client.this);
            return action;
        }

        public Object invoke(Object... args) throws Exception {
            Action action = draft(args);
            return schedule(action);
        }

        public Promise promise(Object... args) throws Exception {
            Action action = draft(args);
            return (Promise) action.execute();
        }

    }

    public class Creator<C extends Creator<C>> {
        protected List<ForePlugin> forePlugins = new ArrayList<ForePlugin>();
        protected List<BackPlugin> backPlugins = new ArrayList<BackPlugin>();

        public C setForePlugins(String... plugins) {
            this.forePlugins.clear();
            return addForePlugins(plugins);
        }

        public C setBackPlugins(String... plugins) {
            this.backPlugins.clear();
            return addBackPlugins(plugins);
        }

        public C addForePlugins(String... plugins) {
            Collections.addAll(forePlugins, load(Arrays.asList(plugins), ForePlugin.class));
            return (C) this;
        }

        public C addBackPlugins(String... plugins) {
            Collections.addAll(backPlugins, load(Arrays.asList(plugins), BackPlugin.class));
            return (C) this;
        }

        public C setForePlugins(ForePlugin... plugins) {
            this.forePlugins.clear();
            return addForePlugins(plugins);
        }

        public C setBackPlugins(BackPlugin... plugins) {
            this.backPlugins.clear();
            return addBackPlugins(plugins);
        }

        public C addForePlugins(ForePlugin... plugins) {
            Collections.addAll(forePlugins, plugins);
            return (C) this;
        }

        public C addBackPlugins(BackPlugin... plugins) {
            Collections.addAll(backPlugins, plugins);
            return (C) this;
        }

        public C setForePlugins(Iterable<ForePlugin> plugins) {
            this.forePlugins.clear();
            return addForePlugins(plugins);
        }

        public C setBackPlugins(Iterable<BackPlugin> plugins) {
            this.backPlugins.clear();
            return addBackPlugins(plugins);
        }

        public C addForePlugins(Iterable<ForePlugin> plugins) {
            Iterator<ForePlugin> iterable = plugins.iterator();
            while (iterable.hasNext()) this.forePlugins.add(iterable.next());
            return (C) this;
        }

        public C addBackPlugins(Iterable<BackPlugin> plugins) {
            Iterator<BackPlugin> iterable = plugins.iterator();
            while (iterable.hasNext()) this.backPlugins.add(iterable.next());
            return (C) this;
        }

        public <T> T create(Class<T> interfase) {
            return create(interfase, protocol, hostname, port, route);
        }

        public <T> T create(Class<T> interfase, String protocol, String host) {
            return create(interfase, protocol, host, null);
        }

        public <T> T create(Class<T> interfase, String protocol, String host, Integer port) {
            return create(interfase, protocol, host, port, null);
        }

        public <T> T create(Class<T> interfase, String protocol, String host, Integer port, String route) {
            String endpoint = protocol + "://" + host + (port != null ? ":" + port : "") + (route != null ? route : "");
            return create(interfase, endpoint);
        }

        public <T> T create(Class<T> interfase, String endpoint) {
            try {
                return create(interfase, new URL(endpoint));
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
        }

        public <T> T create(Class<T> interfase, URL endpoint) {
            String protocol = endpoint.getProtocol();
            String host = endpoint.getHost();
            Integer port = endpoint.getPort() < 0 ? null : endpoint.getPort();
            String route = endpoint.getFile().length() == 0 ? null : endpoint.getFile();
            return new JestfulInvocationHandler<T>(interfase, protocol, host, port, route, Client.this, forePlugins, backPlugins).getProxy();
        }

    }

}
