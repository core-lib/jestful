package org.qfox.jestful.client;

import org.qfox.jestful.client.scheduler.Scheduler;
import org.qfox.jestful.core.*;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by payne on 2017/3/19.
 */
public class JestfulInvocationHandler<T> implements InvocationHandler {
    protected final String protocol;
    protected final String host;
    protected final Integer port;
    protected final String route;
    protected final Client client;
    protected final T proxy;
    protected final Resource resource;

    protected JestfulInvocationHandler(Class<T> interfase, String protocol, String host, Integer port, String route, Client client) {
        if (client.isDestroyed()) {
            throw new IllegalStateException("Client has been destroyed");
        }
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.route = route;
        this.client = client;
        this.proxy = interfase.cast(Proxy.newProxyInstance(client.getClassLoader(), new Class<?>[]{interfase}, this));
        this.resource = new Resource(proxy, interfase);
    }

    public Object invoke(Object target, Method method, Object[] args) throws Throwable {
        if (resource.getMappings().containsKey(method) == false) {
            // Object 方法
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(resource, args);
            }
            // JDK 1.8 default 方法
            else if (Modifier.isAbstract(method.getModifiers()) == false) {
                Class<?> klass = client.getClassLoader().loadClass("java.lang.invoke.MethodHandles$Lookup");
                Constructor<?> constructor = klass.getDeclaredConstructor(Class.class, int.class);
                constructor.setAccessible(true);
                Class<?> interfase = target.getClass().getInterfaces()[0];
                Object lookup = constructor.newInstance(interfase, -1);
                Method special = klass.getMethod("unreflectSpecial", Method.class, Class.class);
                Object handle = special.invoke(lookup, method, interfase);
                Method bind = handle.getClass().getMethod("bindTo", Object.class);
                Object receiver = bind.invoke(handle, target);
                Method invoke = receiver.getClass().getMethod("invokeWithArguments", Object[].class);
                return invoke.invoke(receiver, new Object[]{args});
            }
            // 没有标注http方法注解的方法
            else {
                throw new UnsupportedOperationException();
            }
        }
        if (client.isDestroyed()) {
            throw new IllegalStateException("Client has been destroyed");
        }

        Mapping mapping = resource.getMappings().get(method).clone();
        Collection<Actor> actors = new ArrayList<Actor>(Arrays.asList(client.getPlugins()));
        actors.add(client);
        Action action = new Action(client.getBeanContainer(), actors);
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

        Request request = newRequest(action);
        request.setRequestHeader("User-Agent", client.getUserAgent());
        action.setRequest(request);

        Response response = newResponse(action);
        action.setResponse(response);

        action.setConsumes(mapping.getConsumes());
        action.setProduces(mapping.getProduces());

        action.setAcceptCharsets(new Charsets(client.getAcceptCharsets()));
        action.setAcceptEncodings(new Encodings(client.getAcceptEncodings()));
        action.setAcceptLanguages(new Languages(client.getAcceptLanguages()));
        action.setContentCharsets(new Charsets(client.getContentCharsets()));
        action.setContentEncodings(new Encodings(client.getContentEncodings()));
        action.setContentLanguages(new Languages(client.getContentLanguages()));

        action.setAllowEncode(client.isAllowEncode());
        action.setAcceptEncode(client.isAcceptEncode());

        action.setPathEncodeCharset(client.getPathEncodeCharset());
        action.setQueryEncodeCharset(client.getQueryEncodeCharset());
        action.setHeaderEncodeCharset(client.getHeaderEncodeCharset());

        return doSchedule(action);
    }

    protected Object doSchedule(Action action) throws Exception {
        Result result = action.getResult();
        Body body = result.getBody();
        for (Scheduler scheduler : client.getSchedulers().values()) {
            if (scheduler.supports(action)) {
                Type type = scheduler.getBodyType(client, action);
                body.setType(type);
                Object value = scheduler.schedule(client, action);
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

    protected Request newRequest(Action action) throws Exception {
        return new JestfulClientRequest(action, client, client.getGateway(), client.getConnTimeout(), client.getReadTimeout(), client.getWriteTimeout());
    }

    protected Response newResponse(Action action) throws Exception {
        return new JestfulClientResponse(action, client, client.getGateway());
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

    public Client getClient() {
        return client;
    }

    public T getProxy() {
        return proxy;
    }

    public Resource getResource() {
        return resource;
    }
}
