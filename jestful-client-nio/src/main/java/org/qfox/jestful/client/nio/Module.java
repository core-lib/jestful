package org.qfox.jestful.client.nio;

import org.qfox.jestful.commons.Config;

import java.io.Serializable;

/**
 * Created by yangchangpei on 17/3/28.
 */
public class Module extends Config implements Serializable {
    private static final long serialVersionUID = -3838624370718465752L;

    private static Module instance;

    private String name;
    private String version;
    private String parentName;
    private String parentVersion;

    public static void main(String[] args) {
        new Module();
        new Module();
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

    @Override
    public String toString() {
        return "Module{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", parentName='" + parentName + '\'' +
                ", parentVersion='" + parentVersion + '\'' +
                '}';
    }
}
