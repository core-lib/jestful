package org.qfox.jestful.template.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by yangchangpei on 17/4/19.
 */
public class Layout extends TagSupport {
    private static final long serialVersionUID = 8633349548005127395L;

    private String path;

    @Override
    public int doStartTag() throws JspException {
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            pageContext.forward(path);
            return SKIP_PAGE;
        } catch (Exception e) {
            throw new JspException(e);
        }
    }

    @Override
    public void release() {
        path = null;
        super.release();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
