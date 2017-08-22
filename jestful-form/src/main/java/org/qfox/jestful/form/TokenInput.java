package org.qfox.jestful.form;

import org.qfox.jestful.core.BeanContainer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Enumeration;

/**
 * Created by yangchangpei on 17/8/21.
 */
public class TokenInput extends TagSupport {
    private static final long serialVersionUID = 8901103747024632711L;

    @Override
    public int doStartTag() throws JspException {
        try {
            BeanContainer beanContainer = (BeanContainer) pageContext.getServletContext().getAttribute(BeanContainer.ROOT_BEAN_CONTAINER_CONTEXT_ATTRIBUTE);
            TokenManager tokenManager = beanContainer.get(TokenManager.class);
            String token = tokenManager.create();

            if (getValue("type") == null) setValue("type", "hidden");
            if (getValue("name") == null) setValue("name", "form-token");
            if (getValue("value") == null) setValue("value", token);
            StringBuilder sb = new StringBuilder();
            sb.append("<input");

            Enumeration<String> names = getValues();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                Object value = getValue(name);
                if (value == null) continue;
                else sb.append(" ");
                sb.append(name).append("=").append("\"").append(value.toString()).append("\"");
            }

            sb.append("/>");
            pageContext.getOut().write(sb.toString());
            return SKIP_BODY;
        } catch (Exception e) {
            throw new JspException(e);
        }
    }

}
