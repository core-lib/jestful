package org.qfox.jestful.form;

import org.qfox.jestful.core.BeanContainer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yangchangpei on 17/8/21.
 */
public class TokenInput extends TagSupport implements DynamicAttributes {
    private static final long serialVersionUID = 8901103747024632711L;

    private final Map<String, Object> attributes = new LinkedHashMap<String, Object>();

    @Override
    public int doStartTag() throws JspException {
        try {
            BeanContainer beanContainer = (BeanContainer) pageContext.getServletContext().getAttribute(BeanContainer.ROOT_BEAN_CONTAINER_CONTEXT_ATTRIBUTE);
            TokenManager tokenManager = beanContainer.get(TokenManager.class);

            if (!attributes.containsKey("name")) attributes.put("name", "form-token");
            if (!attributes.containsKey("type")) attributes.put("type", "hidden");
            if (!attributes.containsKey("value")) attributes.put("value", tokenManager.grant());

            StringBuilder sb = new StringBuilder();
            sb.append("<input");
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                String name = entry.getKey();
                Object value = entry.getValue();
                if (value == null) continue;
                else sb.append(" ");
                sb.append(name).append("=").append("\"").append(value.toString()).append("\"");
            }
            sb.append("/>");

            pageContext.getOut().write(sb.toString());
            return SKIP_BODY;
        } catch (Exception e) {
            throw new JspException(e);
        } finally {
            attributes.clear();
        }
    }

    @Override
    public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
        attributes.put(localName, value);
    }

    @Override
    public void release() {
        attributes.clear();
        super.release();
    }
}
