package org.qfox.jestful.client;

import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.Resource;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    protected final List<Actor> forePlugins = new ArrayList<Actor>();
    protected final List<Actor> backPlugins = new ArrayList<Actor>();

    protected JestfulInvocationHandler(Class<T> interfase, String protocol, String host, Integer port, String route, Client client) {
        this(interfase, protocol, host, port, route, client, Collections.<Actor>emptyList(), Collections.<Actor>emptyList());
    }

    protected JestfulInvocationHandler(Class<T> interfase, String protocol, String host, Integer port, String route, Client client, List<Actor> forePlugins, List<Actor> backPlugins) {
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
        this.forePlugins.addAll(forePlugins);
        this.backPlugins.addAll(backPlugins);
    }

    public Object invoke(Object target, Method method, Object[] args) throws Throwable {
        if (!resource.getMappings().containsKey(method)) {
            // Object 方法
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(resource, args);
            }
            // JDK 1.8 default 方法
            else if (!Modifier.isAbstract(method.getModifiers())) {
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

        return client.invoker()
                .setProtocol(protocol)
                .setHost(host)
                .setPort(port)
                .setRoute(route)
                .setResource(resource)
                .setMapping(mapping)
                .setForePlugins(forePlugins)
                .setBackPlugins(backPlugins)
                .invoke(args);
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

    public List<Actor> getForePlugins() {
        return forePlugins;
    }

    public List<Actor> getBackPlugins() {
        return backPlugins;
    }
}
