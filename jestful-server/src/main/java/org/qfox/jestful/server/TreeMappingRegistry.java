package org.qfox.jestful.server;

import java.util.Set;

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

	public Mapping lookup(String method, String uri) throws NotFoundStatusException, BadMethodStatusException {
		Set<Mapping> mappings = tree.match(uri);
		for (Mapping mapping : mappings) {
			if (mapping.getMethod().name().equalsIgnoreCase(method)) {
				return mapping;
			}
		}
		if (mappings.isEmpty()) {
			throw new NotFoundStatusException(uri);
		} else {
			throw new BadMethodStatusException(method, uri, mappings);
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

	public Node<PathExpression, Mapping> getTree() {
		return tree;
	}

	@Override
	public String toString() {
		return tree.toString();
	}

}
