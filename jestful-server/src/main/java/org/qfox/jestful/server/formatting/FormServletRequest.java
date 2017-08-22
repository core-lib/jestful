package org.qfox.jestful.server.formatting;

import org.qfox.jestful.server.JestfulServletRequest;
import org.qfox.jestful.server.ParamServletRequest;

import java.util.Map;

/**
 * Created by yangchangpei on 17/8/22.
 */
public class FormServletRequest extends ParamServletRequest {

    public FormServletRequest(JestfulServletRequest request, Map<String, String[]> parameters) {
        super(request, parameters);
    }
}
