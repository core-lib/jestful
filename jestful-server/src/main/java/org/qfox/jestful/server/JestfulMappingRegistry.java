package org.qfox.jestful.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.qfox.jestful.commons.tree.AlreadyValuedException;
import org.qfox.jestful.commons.tree.Node;
import org.qfox.jestful.commons.tree.PathExpression;
import org.qfox.jestful.core.Accepts;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Resource;
import org.qfox.jestful.core.exception.DuplicateMappingException;
import org.qfox.jestful.core.exception.IllegalConfigException;
import org.qfox.jestful.server.exception.BadMethodStatusException;
import org.qfox.jestful.server.exception.ConflictStatusException;
import org.qfox.jestful.server.exception.NotFoundStatusException;

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
 * @date 2016年4月1日 下午3:09:22
 *
 * @since 1.0.0
 */
public class JestfulMappingRegistry implements MappingRegistry, Initialable {
	private String ctxpath;
	private Node<PathExpression, Mapping> tree;

	public void initialize(BeanContainer beanContainer) {
		ServletContext servletContext = beanContainer.get(ServletContext.class);
		this.ctxpath = servletContext.getContextPath();
		this.tree = new Node<PathExpression, Mapping>(new PathExpression(null));
	}

	public Collection<Mapping> lookup(String URI) throws NotFoundStatusException {
		String path = URI.substring(ctxpath.length());
		Collection<Mapping> mappings = tree.match(path);
		if (mappings.isEmpty()) {
			throw new NotFoundStatusException(URI, null, null);
		} else {
			return mappings;
		}
	}

	public Collection<Mapping> lookup(String method, String URI) throws NotFoundStatusException, BadMethodStatusException {
		Collection<Mapping> matches = new ArrayList<Mapping>();
		Collection<Mapping> mappings = lookup(URI);
		for (Mapping mapping : mappings) {
			if (mapping.getRestful().getMethod().equalsIgnoreCase(method)) {
				matches.add(mapping);
			}
		}
		if (matches.isEmpty()) {
			throw new BadMethodStatusException(URI, method, mappings);
		} else {
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
			Node<PathExpression, Mapping> node = resource.toNode();
			Node<PathExpression, Mapping> parent = new Node<PathExpression, Mapping>(new PathExpression());
			parent.getBranches().add(node);
			tree.merge(parent);
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
	public String toString() {
		return tree.toString();
	}

}
