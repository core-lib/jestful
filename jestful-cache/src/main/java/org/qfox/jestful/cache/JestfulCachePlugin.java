package org.qfox.jestful.cache;

import java.io.File;
import java.util.Map;

import org.qfox.jestful.cache.annotation.Refresh;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Parameters;
import org.qfox.jestful.core.Plugin;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Resource;
import org.qfox.jestful.core.exception.PluginConfigException;

import com.integralblue.httpresponsecache.HttpResponseCache;

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
 * @date 2016年5月31日 下午6:05:31
 *
 * @since 1.0.0
 */
public class JestfulCachePlugin implements Plugin {
	private String directory = System.getProperty("java.io.tmpdir") + "jestful-cache";
	private String capacity = "10MB";

	public void config(Map<String, String> arguments) throws PluginConfigException {
		try {
			this.directory = arguments.containsKey("directory") ? arguments.get("directory") : this.directory;
			this.capacity = arguments.containsKey("capacity") ? arguments.get("capacity") : this.capacity;
			File directory = new File(this.directory);
			long capacity = Capacity.valueOf(this.capacity).toByteSize();
			HttpResponseCache.install(directory, capacity);
		} catch (Exception e) {
			throw new PluginConfigException(e);
		}
	}

	public Object react(Action action) throws Exception {
		Request request = action.getRequest();

		Parameters parameters = action.getParameters();
		for (Parameter parameter : parameters) {
			if (parameter.isAnnotationPresent(Refresh.class) == false) {
				continue;
			}
			Class<?> klass = parameter.getKlass();
			if (klass != boolean.class && klass != Boolean.class) {
				continue;
			}
			Boolean refresh = (Boolean) parameter.getValue();
			if (Boolean.TRUE.equals(refresh)) {
				request.setRequestHeader("Cache-Control", "max-age=0");
			}
			return action.execute();
		}

		Mapping mapping = action.getMapping();
		if (mapping.isAnnotationPresent(Refresh.class)) {
			request.setRequestHeader("Cache-Control", "max-age=0");
			return action.execute();
		}

		Resource resource = action.getResource();
		if (resource.isAnnotationPresent(Refresh.class)) {
			request.setRequestHeader("Cache-Control", "max-age=0");
			return action.execute();
		}

		return action.execute();
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

}
