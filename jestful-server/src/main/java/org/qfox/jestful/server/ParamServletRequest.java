package org.qfox.jestful.server;

import org.qfox.jestful.commons.ArrayKit;
import org.qfox.jestful.commons.collection.Enumerator;

import java.util.*;

/**
 * Created by yangchangpei on 17/8/22.
 */
public class ParamServletRequest extends JestfulServletRequestWrapper {
    private final Map<String, String[]> parameters;

    public ParamServletRequest(JestfulServletRequest request, Map<String, String[]> parameters) {
        super(request);
        this.parameters = parameters != null ? new LinkedHashMap<String, String[]>(parameters) : new LinkedHashMap<String, String[]>();
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (value != null) return value;
        String[] values = parameters.get(name);
        return values != null && values.length > 0 ? values[0] : null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> merged = new LinkedHashMap<String, String[]>();
        Map<String, String[]> map = super.getParameterMap();
        if (map != null) for (Map.Entry<String, String[]> entry : map.entrySet()) merged.put(entry.getKey(), entry.getValue().clone());
        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            String[] values = merged.get(entry.getKey());
            if (values == null) values = entry.getValue().clone();
            else values = ArrayKit.join(values, entry.getValue());
            merged.put(entry.getKey(), values);
        }
        return merged;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        Set<String> keys = new LinkedHashSet<String>();
        Enumeration<String> names = super.getParameterNames();
        if (names != null) while (names.hasMoreElements()) keys.add(names.nextElement());
        keys.addAll(parameters.keySet());
        return new Enumerator<String>(keys);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) return values;
        return parameters.get(name).clone();
    }
}
