package org.qfox.jestful.sample.repository;

import java.util.List;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-02-08 17:50
 **/
public class Topics {
    private List<String> names;

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    @Override
    public String toString() {
        return "Topics{" +
                "names=" + names +
                '}';
    }
}
