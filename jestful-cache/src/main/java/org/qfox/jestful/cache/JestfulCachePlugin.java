package org.qfox.jestful.cache;

import java.io.File;
import java.io.IOException;
import java.net.ResponseCache;
import java.util.Map;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Plugin;
import org.qfox.jestful.core.exception.PluginConfigException;

import com.integralblue.httpresponsecache.HttpResponseCache;

public class JestfulCachePlugin implements Plugin {
	private String directory = System.getProperty("java.io.tmpdir") + "jestful-cache";
	private long maxSize = 20l * 1024l * 1024l;
	private ResponseCache responseCache;

	public void config(Map<String, String> arguments) throws PluginConfigException {
		try {
			this.directory = arguments.containsKey("directory") ? arguments.get("directory") : this.directory;
			this.maxSize = arguments.containsKey("maxSize") ? Long.valueOf(arguments.get("maxSize")) : this.maxSize;
			File directory = new File(this.directory);
			if (directory.exists() == false) {
				directory.mkdirs();
			}
			this.responseCache = HttpResponseCache.install(directory, this.maxSize);
		} catch (IOException e) {
			throw new PluginConfigException(e);
		}
	}

	public Object react(Action action) throws Exception {
		try {
			return action.execute();
		} catch (Exception e) {
			throw e;
		}
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public long getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}

	public ResponseCache getResponseCache() {
		return responseCache;
	}

	public void setResponseCache(ResponseCache responseCache) {
		this.responseCache = responseCache;
	}

}
