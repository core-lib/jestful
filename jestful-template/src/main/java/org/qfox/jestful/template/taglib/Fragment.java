package org.qfox.jestful.template.taglib;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
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

    protected boolean insideOfLayout() {
        Tag parent = getParent();
        while (parent != null) {
            if (parent instanceof Layout) return true;
            else parent = parent.getParent();
        }
        return false;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            // 赋值
            if (insideOfLayout()) {
                // 缓存body
                return EVAL_BODY_BUFFERED;
            }
            // 渲染
            else {
                ServletRequest request = pageContext.getRequest();
                String content = (String) request.getAttribute(name);
                // 如果有值则不需要渲染body了
                if (content != null) {
                    pageContext.getOut().print(content);
                    return SKIP_BODY;
                }
                // 否则还是要渲染body
                else {
                    return EVAL_BODY_BUFFERED;
                }
            }
        } catch (IOException e) {
            throw new JspException(e);
        }
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            // 赋值
            if (insideOfLayout()) {
                if (bodyContent != null) {
                    String content = bodyContent.getString();
                    pageContext.getRequest().setAttribute(name, content);
                }
            }
            // 渲染
            else {
                if (bodyContent != null) {
                    String content = bodyContent.getString();
                    pageContext.getOut().print(content);
                }
            }
            // 继续
            return EVAL_PAGE;
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
