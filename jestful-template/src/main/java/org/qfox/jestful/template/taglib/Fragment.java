package org.qfox.jestful.template.taglib;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * Created by yangchangpei on 17/4/19.
 */
public class Fragment extends BodyTagSupport {
    private static final long serialVersionUID = -4410234723867630259L;

    private String name;

    public Fragment() {
    }

    public Fragment(String name) {
        this.name = name;
    }

    @Override
    public int doStartTag() throws JspException {
        return super.doStartTag();
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            // 赋值
            if (bodyContent != null) {
                String content = bodyContent.getString();
                pageContext.getRequest().setAttribute(name, content);
            }
            // 渲染
            else {
                ServletRequest request = pageContext.getRequest();
                String content = (String) request.getAttribute(name);
                pageContext.getOut().print(content);
            }
            // 继续
            return super.doEndTag();
        } catch (IOException e) {
            throw new JspException(e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}