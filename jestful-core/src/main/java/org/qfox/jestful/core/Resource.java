package org.qfox.jestful.core;

import org.qfox.jestful.commons.tree.Hierarchical;
import org.qfox.jestful.commons.tree.Node;
import org.qfox.jestful.commons.tree.PathExpression;
import org.qfox.jestful.core.annotation.Function;
import org.qfox.jestful.core.annotation.Protocol;
import org.qfox.jestful.core.exception.AmbiguousResourceException;
import org.qfox.jestful.core.exception.IllegalConfigException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * <p>
 * Description: 资源
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年3月31日 上午10:09:23
 * @since 1.0.0
 */
public class Resource extends Configuration implements Hierarchical<PathExpression, Mapping> {
    private final Object controller;
    private final Class<?> klass;
    private final String expression;
    private final Map<Method, List<Mapping>> mappings = new LinkedHashMap<Method, List<Mapping>>();

    public Resource() {
        super(new Annotation[0]);
        this.controller = new Object();
        this.klass = Object.class;
        this.expression = "";
    }

    public Resource(Object controller) throws IllegalConfigException {
        this(controller, controller.getClass());
    }

    public Resource(Object controller, Class<?> klass) throws IllegalConfigException {
        super(klass.getAnnotations());
        try {
            this.controller = controller;
            this.klass = klass;
            Annotation[] protocols = this.getAnnotationsWith(Protocol.class);
            if (protocols == null || protocols.length == 0) {
                String message = String.format(
                        "%s is not a resource controller because it haven't annotation which annotated with @%s",
                        klass.getName(),
                        Protocol.class.getName()
                );
                throw new IllegalConfigException(message, controller);
            }
            if (protocols.length == 1) {
                Annotation protocol = protocols[0];
                String value = protocol.annotationType().getMethod("value").invoke(protocol).toString().trim();
                this.expression = ("/" + value).replaceAll("/+", "/").replaceAll("/+$", "");
                Method[] methods = klass.getMethods();
                for (Method method : methods) {
                    if (method.isSynthetic()) {
                        continue;
                    }
                    Map<Annotation, Method> map = getRestfulMethodFromClasses(protocol.annotationType(), method, klass);
                    if (!map.isEmpty()) {
                        for (Map.Entry<Annotation, Method> entry : map.entrySet()) {
                            Mapping mapping = new Mapping(this, controller, method, entry.getKey(), entry.getValue());
                            List<Mapping> mappings = this.mappings.get(method);
                            if (mappings == null) this.mappings.put(method, mappings = new ArrayList<Mapping>());
                            mappings.add(mapping);
                        }
                        continue;
                    }
                    map = getRestfulMethodFromInterfaces(protocol.annotationType(), method, klass);
                    if (!map.isEmpty()) {
                        for (Map.Entry<Annotation, Method> entry : map.entrySet()) {
                            Mapping mapping = new Mapping(this, controller, method, entry.getKey(), entry.getValue());
                            List<Mapping> mappings = this.mappings.get(method);
                            if (mappings == null) this.mappings.put(method, mappings = new ArrayList<Mapping>());
                            mappings.add(mapping);
                        }
                    }
                }
            } else {
                String message = String.format(
                        "Ambiguous resource %s which has %d controller kind annotations %s",
                        controller.getClass(),
                        protocols.length,
                        Arrays.toString(protocols)
                );
                throw new AmbiguousResourceException(message, controller, this);
            }
        } catch (IllegalConfigException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static Map<Annotation, Method> getRestfulMethodFromClasses(Class<? extends Annotation> protocol, Method method, Class<?> clazz) {
        Class<?> superclass = clazz;

        while (superclass != null) {
            Method[] methods = superclass.getDeclaredMethods();
            flag:
            for (Method m : methods) {
                // 如果方法名或参数个数不一致都是不相等
                if (!m.getName().equals(method.getName()) || m.getParameterTypes().length != method.getParameterTypes().length) continue;
                // 查找被重写的方法
                for (int i = 0; i < m.getParameterTypes().length; i++) if (m.getParameterTypes()[i] != method.getParameterTypes()[i]) continue flag;
                // 在父类中找到了对应的被重写的方法, 判断是否有Command的注解
                Map<Annotation, Method> map = new LinkedHashMap<Annotation, Method>();
                for (Annotation annotation : m.getAnnotations()) {
                    Function function = annotation.annotationType().getAnnotation(Function.class);
                    if (function != null && function.protocol() == protocol) map.put(annotation, m);
                }
                if (!map.isEmpty()) return map;
            }
            superclass = superclass.getSuperclass();
        }

        return Collections.emptyMap();
    }

    private static Map<Annotation, Method> getRestfulMethodFromInterfaces(Class<? extends Annotation> protocol, Method method, Class<?> clazz) {
        Class<?> superclass = clazz;

        while (superclass != null) {
            for (Class<?> interfase : superclass.getInterfaces()) {
                Method[] methods = interfase.getDeclaredMethods();
                flag:
                for (Method m : methods) {
                    // 如果方法名或参数个数不一致都是不相等
                    if (!m.getName().equals(method.getName()) || m.getParameterTypes().length != method.getParameterTypes().length) continue;
                    // 查找被重写的方法
                    for (int i = 0; i < m.getParameterTypes().length; i++) if (m.getParameterTypes()[i] != method.getParameterTypes()[i]) continue flag;
                    // 在父类中找到了对应的被重写的方法, 判断是否有Command的注解
                    Map<Annotation, Method> map = new LinkedHashMap<Annotation, Method>();
                    for (Annotation annotation : m.getAnnotations()) {
                        Function function = annotation.annotationType().getAnnotation(Function.class);
                        if (function != null && function.protocol() == protocol) map.put(annotation, m);
                    }
                    if (!map.isEmpty()) return map;
                    // 深度递归优先
                    map = getRestfulMethodFromInterfaces(protocol, method, interfase);
                    // 如果找到了就返回
                    if (!map.isEmpty()) return map;
                }
            }
            superclass = superclass.getSuperclass();
        }

        return Collections.emptyMap();
    }

    public Node<PathExpression, Mapping> toNode() {
        Node<PathExpression, Mapping> branch = new Node<PathExpression, Mapping>(new PathExpression());
        for (List<Mapping> list : mappings.values()) {
            for (Mapping mapping : list) {
                Node<PathExpression, Mapping> leave = mapping.toNode();
                branch.merge(leave);
            }
        }
        return branch;
    }

    public Object getController() {
        return controller;
    }

    public String getExpression() {
        return expression;
    }

    public Map<Method, List<Mapping>> getMappings() {
        return mappings;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expression == null) ? 0 : expression.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Resource other = (Resource) obj;
        if (expression == null) {
            if (other.expression != null)
                return false;
        } else if (!expression.equals(other.expression))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "{" + expression + " : " + (Proxy.isProxyClass(controller.getClass()) ? klass.getName() : controller) + "}";
    }

}
