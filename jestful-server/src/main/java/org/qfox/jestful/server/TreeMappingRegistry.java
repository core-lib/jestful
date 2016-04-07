package org.qfox.jestful.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.qfox.jestful.server.exception.AlreadyValuedException;
import org.qfox.jestful.server.exception.BadMethodStatusException;
import org.qfox.jestful.server.exception.DuplicateMappingException;
import org.qfox.jestful.server.exception.IllegalConfigException;
import org.qfox.jestful.server.exception.NotFoundStatusException;
import org.qfox.jestful.server.tree.Node;
import org.qfox.jestful.server.tree.PathExpression;

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
public class TreeMappingRegistry implements MappingRegistry {
	private final Node<PathExpression, Mapping> tree;

	public TreeMappingRegistry() {
		this("/");
	}

	public TreeMappingRegistry(String ctxpath) {
		ctxpath = ctxpath == null || ctxpath.trim().isEmpty() || ctxpath.trim().equals("/") ? "" : ctxpath.trim();
		this.tree = new Node<PathExpression, Mapping>(new PathExpression(ctxpath));
	}

	public Collection<Mapping> lookup(String URI) throws NotFoundStatusException {
		return tree.match(URI);
	}

	public Mapping lookup(String command, String URI) throws NotFoundStatusException, BadMethodStatusException {
		Collection<Mapping> mappings = lookup(URI);
		for (Mapping mapping : mappings) {
			if (mapping.getCommand().name().equalsIgnoreCase(command)) {
				return mapping;
			}
		}
		if (mappings.isEmpty()) {
			throw new NotFoundStatusException(URI);
		} else {
			throw new BadMethodStatusException(command, URI, mappings);
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

	public Node<PathExpression, Mapping> getTree() {
		return tree;
	}

	@Override
	public String toString() {
		return tree.toString();
	}

}
