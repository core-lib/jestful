package org.qfox.jestful.client;

import org.qfox.jestful.client.exception.NoSuchSerializerException;
import org.qfox.jestful.client.exception.UnexpectedStatusException;
import org.qfox.jestful.client.exception.UnexpectedTypeException;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.client.scheduler.Scheduler;
import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.NoSuchCharsetException;
import org.qfox.jestful.core.formatting.RequestSerializer;
import org.qfox.jestful.core.formatting.ResponseDeserializer;
import org.qfox.jestful.commons.IOUtils;
import org.qfox.jestful.core.io.RequestLazyOutputStream;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.*;
import java.util.Map.Entry;

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
public class Client implements Actor, Connector, Initialable, Destroyable {
    private final Charsets charsets = new Charsets(Charset.availableCharsets().keySet().toArray(new String[0]));
    private final Map<MediaType, RequestSerializer> serializers = new HashMap<MediaType, RequestSerializer>();
    private final Map<MediaType, ResponseDeserializer> deserializers = new HashMap<MediaType, ResponseDeserializer>();
    private final Map<String, Scheduler> schedulers = new HashMap<String, Scheduler>();
    private final Map<String, Connector> connectors = new HashMap<String, Connector>();

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

    private final boolean allowEncode;
    private final boolean acceptEncode;

    private final String pathEncodeCharset;
    private final String queryEncodeCharset;
    private final String headerEncodeCharset;

    private final int connTimeout;
    private final int readTimeout;
    private final int writeTimeout;

    private final Gateway gateway;
    private final HostnameVerifier hostnameVerifier;
    private final SSLSocketFactory SSLSocketFactory;

    private boolean destroyed = false;

    private static Client defaultClient;

    protected Client(Builder<?> builder) {
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

        this.pathEncodeCharset = builder.pathEncodeCharset;
        this.queryEncodeCharset = builder.queryEncodeCharset;
        this.headerEncodeCharset = builder.headerEncodeCharset;

        this.connTimeout = builder.connTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;

        this.gateway = builder.gateway;
        this.hostnameVerifier = builder.hostnameVerifier;
        this.SSLSocketFactory = builder.SSLSocketFactory;

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

        Map<String, Connector> connectors = beanContainer.find(Connector.class);
        this.connectors.putAll(connectors);

        Map<String, Scheduler> schedulers = beanContainer.find(Scheduler.class);
        this.schedulers.putAll(schedulers);
    }

    @Override
    public void destroy() {
        if (destroyed) {
            return;
        }
        destroyed = true;
        Collection<Destroyable> destroyables = this.beanContainer.find(Destroyable.class).values();
        for (Destroyable destroyable : destroyables) {
            destroyable.destroy();
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    protected void finalize() throws Throwable {
        this.destroy();
        super.finalize();
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
        throw new IOException("unsupported protocol " + action.getProtocol());
    }

    protected void serialize(Action action) throws Exception {
        Request request = action.getRequest();
        List<Parameter> bodies = action.getParameters().all(Position.BODY);
        if (bodies.isEmpty()) {
            request.connect();
            return;
        } else {
            String charset = null;
            Charsets options = action.getContentCharsets().clone();
            if (options.isEmpty() == false) {
                options.retainAll(charsets);
                if (options.isEmpty()) {
                    throw new NoSuchCharsetException(action.getContentCharsets().clone(), charsets.clone());
                }
                charset = options.first().getName();
            } else {
                charset = Charset.defaultCharset().name();
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
                        IOUtils.close(out);
                    }
                }
            }
            throw new NoSuchSerializerException(action, null, consumes, serializers.values());
        }
    }

    protected void deserialize(Action action) throws Exception {
        Response response = action.getResponse();
        Body body = action.getResult().getBody();
        if (body.getType() == Void.TYPE) {
            return;
        } else {
            String contentType = response.getContentType();
            Accepts produces = action.getProduces();
            Accepts supports = new Accepts(deserializers.keySet());
            MediaType mediaType = MediaType.valueOf(contentType);
            if ((produces.isEmpty() || produces.contains(mediaType)) && supports.contains(mediaType)) {
                String charset = mediaType.getCharset();
                if (charset == null || charset.length() == 0) {
                    charset = response.getResponseHeader("Content-Charset");
                }
                if (charset == null || charset.length() == 0) {
                    charset = response.getCharacterEncoding();
                }
                if (charset == null || charset.length() == 0) {
                    charset = Charset.defaultCharset().name();
                }
                ResponseDeserializer deserializer = deserializers.get(mediaType);
                InputStream in = response.getResponseInputStream();
                deserializer.deserialize(action, mediaType, charset, in);
                return;
            } else if (body.getType() == String.class) {
                String charset = mediaType.getCharset();
                if (charset == null || charset.length() == 0) {
                    charset = response.getResponseHeader("Content-Charset");
                }
                if (charset == null || charset.length() == 0) {
                    charset = response.getCharacterEncoding();
                }
                if (charset == null || charset.length() == 0) {
                    charset = Charset.defaultCharset().name();
                }
                InputStream in = response.getResponseInputStream();
                InputStreamReader reader = new InputStreamReader(in, charset);
                String value = IOUtils.toString(reader);
                body.setValue(value);
            } else if (produces.size() == 1) {
                String charset = mediaType.getCharset();
                mediaType = produces.iterator().next();
                if (charset == null || charset.length() == 0) {
                    charset = response.getResponseHeader("Content-Charset");
                }
                if (charset == null || charset.length() == 0) {
                    charset = response.getCharacterEncoding();
                }
                if (charset == null || charset.length() == 0) {
                    charset = mediaType.getCharset();
                }
                if (charset == null || charset.length() == 0) {
                    charset = Charset.defaultCharset().name();
                }
                ResponseDeserializer deserializer = deserializers.get(mediaType);
                InputStream in = response.getResponseInputStream();
                deserializer.deserialize(action, mediaType, charset, in);
                return;
            } else {
                if (produces.isEmpty() == false) {
                    supports.retainAll(produces);
                }
                throw new UnexpectedTypeException(mediaType, supports);
            }
        }
    }

    public Object react(Action action) throws Exception {
        Request request = action.getRequest();
        Response response = action.getResponse();
        try {
            Restful restful = action.getRestful();

            if (restful.isAcceptBody()) {
                serialize(action);
            } else {
                request.connect();
            }

            if (!response.isResponseSuccess()) {
                String contentType = response.getContentType();
                MediaType mediaType = contentType == null || contentType.trim().length() == 0 ? null : MediaType.valueOf(contentType);
                String charset = mediaType == null ? null : mediaType.getCharset();
                if (charset == null || charset.length() == 0) {
                    charset = response.getResponseHeader("Content-Charset");
                }
                if (charset == null || charset.length() == 0) {
                    charset = response.getCharacterEncoding();
                }
                if (charset == null || charset.length() == 0) {
                    charset = Charset.defaultCharset().name();
                }
                Status status = response.getResponseStatus();
                InputStream in = response.getResponseInputStream();
                InputStreamReader reader = in == null ? null : new InputStreamReader(in, charset);
                String body = reader != null ? IOUtils.toString(reader) : "";
                throw new UnexpectedStatusException(status, body);
            }

            // 回应
            if (restful.isReturnBody()) {
                deserialize(action);
            } else {
                Map<String, String> header = new CaseInsensitiveMap<String, String>();
                for (String key : response.getHeaderKeys()) {
                    String name = key != null ? key : "";
                    String value = response.getResponseHeader(key);
                    header.put(name, value);
                }
                return header;
            }

            // 返回
            return action.getResult().getBody().getValue();
        } catch (Exception e) {
            throw e;
        } finally {
            request.close();
            response.close();
        }
    }

    public <T> T create(Class<T> interfase) {
        return create(interfase, protocol, host, port, route);
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
        return new JestfulInvocationHandler<T>(interfase, protocol, host, port, route, this).getProxy();
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

    public static class Builder<T extends Builder<T>> {
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

        private boolean allowEncode = false;
        private boolean acceptEncode = true;

        private String pathEncodeCharset = "UTF-8";
        private String queryEncodeCharset = "UTF-8";
        private String headerEncodeCharset = "UTF-8";

        private int connTimeout = 0;
        private int readTimeout = 0;
        private int writeTimeout = 0;

        private Gateway gateway = Gateway.NULL;

        private HostnameVerifier hostnameVerifier;
        private SSLSocketFactory SSLSocketFactory;

        public Client build() {
            return new Client(this);
        }

        public T setEndpoint(URL endpoint) {
            if (endpoint == null) {
                throw new IllegalArgumentException("endpoint == null");
            }
            setProtocol(endpoint.getProtocol());
            setHost(endpoint.getHost());
            setPort(endpoint.getPort() < 0 ? null : endpoint.getPort());
            setRoute(endpoint.getFile().length() == 0 ? null : endpoint.getFile());
            return (T) this;
        }

        public T setProtocol(String protocol) {
            if (protocol == null) {
                throw new IllegalArgumentException("protocol == null");
            }
            this.protocol = protocol;
            return (T) this;
        }

        public T setHost(String host) {
            if (protocol == null) {
                throw new IllegalArgumentException("host == null");
            }
            this.host = host;
            return (T) this;
        }

        public T setPort(Integer port) {
            if (port != null && (port < 0 || port > 65535)) {
                throw new IllegalArgumentException("port " + port + " out of bounds [0, 65535]");
            }
            this.port = port;
            return (T) this;
        }

        public T setRoute(String route) {
            if (route != null && route.length() == 0 == false && route.startsWith("/") == false) {
                throw new IllegalArgumentException("route should starts with /");
            }
            this.route = route;
            return (T) this;
        }

        public T setClassLoader(ClassLoader classLoader) {
            if (classLoader == null) {
                throw new IllegalArgumentException("class loader is null");
            }
            this.classLoader = classLoader;
            return (T) this;
        }

        public T setBeanContainer(String beanContainer) {
            if (beanContainer == null) {
                throw new IllegalArgumentException("bean container is null");
            }
            this.beanContainer = beanContainer;
            return (T) this;
        }

        public T setPlugins(String... plugins) {
            if (plugins == null || plugins.length == 0) {
                throw new IllegalArgumentException("plugins is null or empty array");
            }
            this.plugins = new ArrayList<String>(Arrays.asList(plugins));
            return (T) this;
        }

        public T addPlugins(String... plugins) {
            if (plugins == null) {
                throw new IllegalArgumentException("plugins is null");
            }
            this.plugins.addAll(Arrays.asList(plugins));
            return (T) this;
        }

        public T setConfigLocations(String... configLocations) {
            if (configLocations == null || configLocations.length == 0) {
                throw new IllegalArgumentException("config locations is null or empty");
            }
            this.configLocations = new ArrayList<String>(Arrays.asList(configLocations));
            return (T) this;
        }

        public T addConfigLocations(String... configLocations) {
            if (configLocations == null) {
                throw new IllegalArgumentException("config locations is null");
            }
            this.configLocations.addAll(Arrays.asList(configLocations));
            return (T) this;
        }

        public T setAcceptCharsets(String... acceptCharsets) {
            if (acceptCharsets == null || acceptCharsets.length == 0) {
                throw new IllegalArgumentException("accept charsets is null or empty");
            }
            this.acceptCharsets = new ArrayList<String>(Arrays.asList(acceptCharsets));
            return (T) this;
        }

        public T addAcceptCharsets(String... acceptCharsets) {
            if (acceptCharsets == null) {
                throw new IllegalArgumentException("accept charsets is null");
            }
            this.acceptCharsets.addAll(Arrays.asList(acceptCharsets));
            return (T) this;
        }

        public T setAcceptEncodings(String... acceptEncodings) {
            if (acceptEncodings == null || acceptEncodings.length == 0) {
                throw new IllegalArgumentException("accept encodings is null or empty");
            }
            this.acceptEncodings = new ArrayList<String>(Arrays.asList(acceptEncodings));
            return (T) this;
        }

        public T addAcceptEncodings(String... acceptEncodings) {
            if (acceptEncodings == null) {
                throw new IllegalArgumentException("accept encodings is null");
            }
            this.acceptEncodings.addAll(Arrays.asList(acceptEncodings));
            return (T) this;
        }

        public T setAcceptLanguages(String... acceptLanguages) {
            if (acceptLanguages == null || acceptLanguages.length == 0) {
                throw new IllegalArgumentException("accept languages is null or empty");
            }
            this.acceptLanguages = new ArrayList<String>(Arrays.asList(acceptLanguages));
            return (T) this;
        }

        public T addAcceptLanguages(String... acceptLanguages) {
            if (acceptLanguages == null) {
                throw new IllegalArgumentException("accept languages is null");
            }
            this.acceptLanguages.addAll(Arrays.asList(acceptLanguages));
            return (T) this;
        }

        public T setContentCharsets(String... contentCharsets) {
            if (contentCharsets == null || contentCharsets.length == 0) {
                throw new IllegalArgumentException("content charsets is null or empty");
            }
            this.contentCharsets = new ArrayList<String>(Arrays.asList(contentCharsets));
            return (T) this;
        }

        public T addContentCharsets(String... contentCharsets) {
            if (contentCharsets == null) {
                throw new IllegalArgumentException("content charsets is null");
            }
            this.contentCharsets.addAll(Arrays.asList(contentCharsets));
            return (T) this;
        }

        public T setContentEncodings(String... contentEncodings) {
            if (contentEncodings == null || contentEncodings.length == 0) {
                throw new IllegalArgumentException("content encodings is null or empty");
            }
            this.contentEncodings = new ArrayList<String>(Arrays.asList(contentEncodings));
            return (T) this;
        }

        public T addContentEncodings(String... contentEncodings) {
            if (contentEncodings == null) {
                throw new IllegalArgumentException("content encodings is null");
            }
            this.contentEncodings.addAll(Arrays.asList(contentEncodings));
            return (T) this;
        }

        public T setContentLanguages(String... contentLanguages) {
            if (contentLanguages == null || contentLanguages.length == 0) {
                throw new IllegalArgumentException("content languages is null or empty");
            }
            this.contentLanguages = new ArrayList<String>(Arrays.asList(contentLanguages));
            return (T) this;
        }

        public T addContentLanguages(String... contentLanguages) {
            if (contentLanguages == null) {
                throw new IllegalArgumentException("content languages is null");
            }
            this.contentLanguages.addAll(Arrays.asList(contentLanguages));
            return (T) this;
        }

        public T setAllowEncode(boolean allowEncode) {
            this.allowEncode = allowEncode;
            return (T) this;
        }

        public T setAcceptEncode(boolean acceptEncode) {
            this.acceptEncode = acceptEncode;
            return (T) this;
        }

        public T setPathEncodeCharset(String pathEncodeCharset) {
            if (Charset.isSupported(pathEncodeCharset) == false) {
                throw new UnsupportedCharsetException(pathEncodeCharset);
            }
            this.pathEncodeCharset = pathEncodeCharset;
            return (T) this;
        }

        public T setQueryEncodeCharset(String queryEncodeCharset) {
            if (Charset.isSupported(queryEncodeCharset) == false) {
                throw new UnsupportedCharsetException(queryEncodeCharset);
            }
            this.queryEncodeCharset = queryEncodeCharset;
            return (T) this;
        }

        public T setHeaderEncodeCharset(String headerEncodeCharset) {
            if (Charset.isSupported(headerEncodeCharset) == false) {
                throw new UnsupportedCharsetException(headerEncodeCharset);
            }
            this.headerEncodeCharset = headerEncodeCharset;
            return (T) this;
        }

        public T setConnTimeout(int connTimeout) {
            if (connTimeout < 0) {
                throw new IllegalArgumentException("connect timeout is negative");
            }
            this.connTimeout = connTimeout;
            return (T) this;
        }

        public T setReadTimeout(int readTimeout) {
            if (readTimeout < 0) {
                throw new IllegalArgumentException("reading timeout is negative");
            }
            this.readTimeout = readTimeout;
            return (T) this;
        }

        public T setWriteTimeout(int writeTimeout) {
            if (writeTimeout < 0) {
                throw new IllegalArgumentException("writing timeout is negative");
            }
            this.writeTimeout = writeTimeout;
            return (T) this;
        }

        public T setGateway(Gateway gateway) {
            if (gateway == null) {
                throw new IllegalArgumentException("can not set null gateway");
            }
            this.gateway = gateway;
            return (T) this;
        }

        public T setHostnameVerifier(HostnameVerifier hostnameVerifier) {
            if (hostnameVerifier == null) {
                throw new IllegalArgumentException("can not set null hostnameVerifier");
            }
            this.hostnameVerifier = hostnameVerifier;
            return (T) this;
        }

        public T setSSLSocketFactory(SSLSocketFactory SSLSocketFactory) {
            if (SSLSocketFactory == null) {
                throw new IllegalArgumentException("can not set null SSLSocketFactory");
            }
            this.SSLSocketFactory = SSLSocketFactory;
            return (T) this;
        }

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

    public BeanContainer getBeanContainer() {
        return beanContainer;
    }

    public String[] getConfigLocations() {
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

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public SSLSocketFactory getSSLSocketFactory() {
        return SSLSocketFactory;
    }

    @Override
    public String toString() {
        return protocol + "://" + host + (port != null ? ":" + port : "") + (route != null ? route : "");
    }

}
