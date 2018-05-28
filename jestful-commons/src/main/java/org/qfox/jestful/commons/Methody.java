package org.qfox.jestful.commons;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 方法
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-28 13:41
 **/
public class Methody implements Serializable {
    private static final long serialVersionUID = -4171594861016715278L;

    private final String signature;
    private final List<String> variables;

    Methody(String signature, List<String> variables) {
        this.signature = signature;
        this.variables = variables != null ? Collections.unmodifiableList(variables) : null;
    }

    public String getSignature() {
        return signature;
    }

    public List<String> getVariables() {
        return variables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Methody methody = (Methody) o;

        return signature != null ? signature.equals(methody.signature) : methody.signature == null;
    }

    @Override
    public int hashCode() {
        return signature != null ? signature.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.valueOf(signature);
    }
}
