package org.qfox.jestful.server.formatting;

import org.qfox.jestful.commons.collection.Enumerator;
import org.qfox.jestful.server.JestfulServletRequest;
import org.qfox.jestful.server.JestfulServletRequestWrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by yangchangpei on 17/8/17.
 */
public class URLEncodedServletRequest extends JestfulServletRequestWrapper {
    private final Map<String, String[]> parameters;

    public URLEncodedServletRequest(JestfulServletRequest request, Map<String, String[]> parameters) {
        super(request);
        this.parameters = Collections.unmodifiableMap(parameters);
    }

    @Override
    public String getParameter(String name) {
        String[] values = parameters.get(name);
        return values != null && values.length > 0 ? values[0] : null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameters;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return new Enumerator<String>(parameters.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        return parameters.get(name);
    }
}
