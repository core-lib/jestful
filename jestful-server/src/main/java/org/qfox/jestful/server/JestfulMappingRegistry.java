package org.qfox.jestful.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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
import org.qfox.jestful.core.formatting.RequestDeserializer;
import org.qfox.jestful.server.exception.BadMethodStatusException;
import org.qfox.jestful.server.exception.NotAcceptableStatusException;
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
	private final Map<MediaType, RequestDeserializer> map = new HashMap<MediaType, RequestDeserializer>();
	private Node<PathExpression, Mapping> tree;

	public void initialize(BeanContainer beanContainer) {
		ServletContext servletContext = beanContainer.get(ServletContext.class);
		String ctxpath = servletContext.getContextPath();
		ctxpath = ctxpath == null || ctxpath.trim().isEmpty() || ctxpath.trim().equals("/") ? "" : ctxpath.trim();
		this.tree = new Node<PathExpression, Mapping>(new PathExpression(ctxpath));

		Collection<RequestDeserializer> deserializers = beanContainer.find(RequestDeserializer.class).values();
		for (RequestDeserializer deserializer : deserializers) {
			String contentType = deserializer.getContentType();
			MediaType mediaType = MediaType.valueOf(contentType);
			map.put(mediaType, deserializer);
		}
	}

	public Collection<Mapping> lookup(String URI) throws NotFoundStatusException {
		Collection<Mapping> mappings = tree.match(URI);
		if (mappings.isEmpty()) {
			throw new NotFoundStatusException(URI, null);
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

	public Mapping lookup(String method, String URI, String accept, Comparator<String> comparator) throws NotFoundStatusException, BadMethodStatusException, NotAcceptableStatusException {
		Collection<Mapping> mappings = lookup(method, URI);
		Accepts accepts = Accepts.valueOf(accept);
		Accepts supports = new Accepts(map.keySet());
		// 如果没有Accept请求头 拿最新的
		if (accepts.isEmpty()) {
			Mapping latest = null;
			for (Mapping mapping : mappings) {
				if (latest == null) {
					latest = mapping;
				} else if (comparator.compare(latest.getVersion(), mapping.getVersion()) < 0) {
					latest = mapping;
				}
			}
			return latest;
		}
		for (MediaType mediaType : accepts) {
			String version = mediaType.getVersion();
			if (version == null) {
				// 有版本号比没版本号更新, 请求没带版本号默认拿最新的也就是有版本号的而且版本号最大的
				Mapping latest = null;
				for (Mapping mapping : mappings) {
					Accepts produces = mapping.getProduces();
					if ((mediaType.isWildcard() || supports.contains(mediaType)) && (produces.isEmpty() || produces.contains(mediaType))) {
						if (latest == null) {
							latest = mapping;
						} else if (comparator.compare(latest.getVersion(), mapping.getVersion()) < 0) {
							latest = mapping;
						}
					} else {
						continue;
					}
				}
				if (latest != null) {
					return latest;
				}
			} else {
				for (Mapping mapping : mappings) {
					String ver = mapping.getVersion();
					if (version.equals(ver)) {
						Accepts produces = mapping.getProduces();
						if ((mediaType.isWildcard() || supports.contains(mediaType)) && (produces.isEmpty() || produces.contains(mediaType))) {
							return mapping;
						} else {
							continue;
						}
					}
				}
			}
		}
		throw new NotAcceptableStatusException(URI, method, accepts, null);
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
