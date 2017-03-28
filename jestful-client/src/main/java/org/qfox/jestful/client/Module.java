package org.qfox.jestful.client;

import org.qfox.jestful.commons.Config;

import java.io.Serializable;

/**
 * Created by payne on 2017/3/28.
 */
public class Module extends Config implements Serializable {
    private static final long serialVersionUID = 1276714617922958064L;

    private static Module instance;

    private String name;
    private String version;
    private String parentName;
    private String parentVersion;

    public Module() {
    }

    public static Module getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (Module.class) {
            if (instance != null) {
                return instance;
            }
            return instance = new Module();
        }
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getParentName() {
        return parentName;
    }

    public String getParentVersion() {
        return parentVersion;
    }
}
