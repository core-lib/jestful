package org.qfox.jestful.cache;

import java.io.File;
import java.util.Map;

import org.qfox.jestful.cache.annotation.Cache;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.Plugin;
import org.qfox.jestful.core.Restful;
import org.qfox.jestful.core.exception.PluginConfigException;

public class JestfulCachePlugin implements Plugin {
	private String directory = System.getProperty("java.io.tmpdir") + "jestful" + File.separator + "cache";

	public void config(Map<String, String> arguments) throws PluginConfigException {
		directory = arguments.containsKey("directory") ? arguments.get("directory") : directory;
	}

	public Object react(Action action) throws Exception {
		Restful restful = action.getRestful();
		String method = restful.getMethod();
		Mapping mapping = action.getMapping();
		if ("GET".equalsIgnoreCase(method) && mapping.isAnnotationPresent(Cache.class)) {
			String protocol = action.getProtocol();
			String host = action.getHost();
			Integer port = action.getPort();
			String route = action.getRoute();
			String URI = action.getURI();
			String query = action.getQuery();
			String subpath = "/" + protocol + "/" + (port != null ? host + ":" + port : host) + (route != null ? "/" + route : "") + URI + (query != null ? "/" + query : "");
			String abspath = directory + subpath.replace("/", File.separator);
			System.out.println(abspath);
		}
		return action.execute();
	}
}
