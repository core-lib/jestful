package org.qfox.jestful.server;

import org.qfox.jestful.commons.collection.Enumerator;
import org.qfox.jestful.commons.tree.AlreadyValuedException;
import org.qfox.jestful.commons.tree.Node;
import org.qfox.jestful.commons.tree.PathExpression;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.DuplicateMappingException;
import org.qfox.jestful.core.exception.IllegalConfigException;
import org.qfox.jestful.server.exception.BadMethodStatusException;
import org.qfox.jestful.server.exception.ConflictStatusException;
import org.qfox.jestful.server.exception.NotFoundStatusException;

import javax.servlet.ServletContext;
import java.util.*;

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
 * @date 2016年4月1日 下午3:09:22
 * @since 1.0.0
 */
public class JestfulMappingRegistry implements MappingRegistry, Initialable {
    private String context = "";
    private Node<PathExpression, Mapping> tree;

    public void initialize(BeanContainer beanContainer) {
        ServletContext servletContext = beanContainer.get(ServletContext.class);
        this.context = servletContext.getContextPath() != null ? servletContext.getContextPath() : "";
        this.tree = new Node<PathExpression, Mapping>(new PathExpression(null));
    }

    public Collection<Mapping> lookup(String URI) throws NotFoundStatusException {
        String path = URI.substring(context.length());
        Collection<Mapping> mappings = tree.match(path);
        if (mappings.isEmpty()) {
            throw new NotFoundStatusException(URI, null, null);
        } else {
            return mappings;
        }
    }

    public Collection<Mapping> lookup(String method, String URI) throws NotFoundStatusException, BadMethodStatusException {
        List<Mapping> matches = new ArrayList<Mapping>();
        Collection<Mapping> mappings = lookup(URI);
        for (Mapping mapping : mappings) {
            if (mapping.getRestful().getMethod().equalsIgnoreCase(method)) {
                matches.add(mapping);
            }
        }
        if (matches.isEmpty()) {
            throw new BadMethodStatusException(URI, method, mappings);
        } else {
            Collections.sort(matches);
            return matches;
        }
    }

    public Mapping lookup(String method, String URI, String accept, Comparator<String> comparator) throws NotFoundStatusException, BadMethodStatusException, ConflictStatusException {
        Accepts accepts = Accepts.valueOf(accept);
        Set<String> versions = new LinkedHashSet<String>();
        for (MediaType mediaType : accepts) {
            versions.add(mediaType.getVersion());
        }
        if (versions.size() > 1) {
            throw new ConflictStatusException(URI, method, versions);
        }
        String version = versions.isEmpty() ? null : versions.toArray(new String[1])[0];

        Collection<Mapping> mappings = lookup(method, URI);
        if (version == null) {
            Mapping latest = null;
            for (Mapping mapping : mappings) {
                if (latest == null) {
                    latest = mapping;
                } else if (comparator.compare(latest.getVersion(), mapping.getVersion()) < 0) {
                    latest = mapping;
                }
            }
            return latest;
        } else {
            for (Mapping mapping : mappings) {
                if (version.equals(mapping.getVersion())) {
                    return mapping;
                }
            }
            throw new NotFoundStatusException(URI, method, version);
        }
    }

    public Resource register(Object controller) throws IllegalConfigException {
        try {
            Resource resource = new Resource(controller);
            Node<PathExpression, Mapping> branch = resource.toNode();
            tree.merge(branch);
            return resource;
        } catch (AlreadyValuedException e) {
            Node<?, ?> current = e.getCurrent();
            Node<?, ?> existed = e.getExisted();
            throw new DuplicateMappingException(e, (Mapping) current.getValue(), (Mapping) existed.getValue());
        }
    }

    public Collection<Resource> register(Object... controllers) throws IllegalConfigException {
        return register(Arrays.asList(controllers));
    }

    public Collection<Resource> register(Collection<Object> controllers) throws IllegalConfigException {
        Collection<Resource> resources = new ArrayList<Resource>();
        for (Object controller : controllers) {
            Resource resource = register(controller);
            resources.add(resource);
        }
        return resources;
    }

    @Override
    public Enumeration<Mapping> enumeration() {
        return new Enumerator<Mapping>(all(tree));
    }

    private List<Mapping> all(Node<PathExpression, Mapping> node) {
        List<Mapping> mappings = new ArrayList<Mapping>();
        if (node.getValue() != null) mappings.add(node.getValue());
        for (Node<PathExpression, Mapping> branch : node.getBranches()) mappings.addAll(all(branch));
        return mappings;
    }

    @Override
    public String toString() {
        return tree.toString();
    }

}
