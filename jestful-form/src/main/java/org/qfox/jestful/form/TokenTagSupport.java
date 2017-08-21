package org.qfox.jestful.form;

import org.qfox.jestful.core.BeanContainer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by yangchangpei on 17/8/21.
 */
public class TokenTagSupport extends TagSupport {
    private static final long serialVersionUID = 8901103747024632711L;

    private static final String DEFAULT_TOKEN_NAME = "token";

    private String name = DEFAULT_TOKEN_NAME;

    @Override
    public int doStartTag() throws JspException {
        try {
            BeanContainer beanContainer = (BeanContainer) pageContext.getServletContext().getAttribute(BeanContainer.ROOT_BEAN_CONTAINER_CONTEXT_ATTRIBUTE);
            TokenManager tokenManager = beanContainer.get(TokenManager.class);
            String token = tokenManager.create();
            String input = "<input type='hidden' name='" + name + "' value='" + token + "' />";
            pageContext.getOut().write(input);
            return SKIP_BODY;
        } catch (Exception e) {
            throw new JspException(e);
        }
    }

    @Override
    public void release() {
        this.name = DEFAULT_TOKEN_NAME;
        super.release();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
