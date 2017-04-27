package org.qfox.jestful.android;

import java.io.File;
import java.util.Map;

import org.qfox.jestful.android.annotation.Refresh;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Parameters;
import org.qfox.jestful.core.Plugin;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Resource;
import org.qfox.jestful.core.exception.BeanConfigException;

import android.net.http.HttpResponseCache;

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
public class AndroidCachePlugin implements Plugin {
	private String directory = System.getProperty("java.io.tmpdir") + "jestful-cache";
	private String capacity = "10MB";

	public void config(Map<String, String> arguments) throws BeanConfigException {
		try {
			this.directory = arguments.containsKey("directory") ? arguments.get("directory") : this.directory;
			this.capacity = arguments.containsKey("capacity") ? arguments.get("capacity") : this.capacity;
			File directory = new File(this.directory);
			long capacity = Capacity.valueOf(this.capacity).toByteSize();
			HttpResponseCache.install(directory, capacity);
		} catch (Exception e) {
			throw new BeanConfigException(e);
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
			Refresh refresh = parameter.getAnnotation(Refresh.class);
			Object value = parameter.getValue();
			if (value != null && value.equals(true)) {
				request.setRequestHeader("Cache-Control", refresh.value());
			}
			return action.execute();
		}

		Mapping mapping = action.getMapping();
		if (mapping.isAnnotationPresent(Refresh.class)) {
			Refresh refresh = mapping.getAnnotation(Refresh.class);
			request.setRequestHeader("Cache-Control", refresh.value());
			return action.execute();
		}

		Resource resource = action.getResource();
		if (resource.isAnnotationPresent(Refresh.class)) {
			Refresh refresh = resource.getAnnotation(Refresh.class);
			request.setRequestHeader("Cache-Control", refresh.value());
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
