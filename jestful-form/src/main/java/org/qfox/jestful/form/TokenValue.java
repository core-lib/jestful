package org.qfox.jestful.form;

import org.qfox.jestful.core.BeanContainer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by yangchangpei on 17/8/22.
 */
public class TokenValue extends TagSupport {

    @Override
    public int doStartTag() throws JspException {
        try {
            BeanContainer beanContainer = (BeanContainer) pageContext.getServletContext().getAttribute(BeanContainer.ROOT_BEAN_CONTAINER_CONTEXT_ATTRIBUTE);
            TokenManager tokenManager = beanContainer.get(TokenManager.class);
            String token = tokenManager.create();
            pageContext.getOut().write(token);
            return SKIP_BODY;
        } catch (Exception e) {
            throw new JspException(e);
        }
    }

}
