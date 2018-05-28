package org.qfox.jestful.commons;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * 字节码
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-28 13:41
 **/
public class Classy implements Serializable {
    private static final long serialVersionUID = 5624831558512439085L;

    private final String name;
    private final Map<String, Methody> methods;

    Classy(String name, Map<String, Methody> methods) {
        this.name = name;
        this.methods = methods != null ? Collections.unmodifiableMap(methods) : null;
    }

    public String getName() {
        return name;
    }

    public Map<String, Methody> getMethods() {
        return methods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Classy classy = (Classy) o;

        return name != null ? name.equals(classy.name) : classy.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }
}
