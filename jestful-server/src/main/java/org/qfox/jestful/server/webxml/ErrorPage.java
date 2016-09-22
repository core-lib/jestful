package org.qfox.jestful.server.webxml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by yangchangpei on 16/9/21.
 */
public class ErrorPage {
    private String code;
    private Class<?> type;
    private String location;

    public ErrorPage(Element element) throws ClassNotFoundException {
        NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String name = node.getNodeName();
            if ("error-page".equals(name)) {
                this.code = node.getTextContent().trim();
            } else if ("exception-type".equals(name)) {
                this.type = Class.forName(node.getTextContent().trim());
            } else if ("location".equals(name)) {
                this.location = node.getTextContent().trim();
            }
        }
    }

    public boolean isMatch(int code) {
        String regex = this.code == null ? "" : this.code.replaceAll("[xX]", "[0-9]");
        return String.valueOf(code).matches(regex);
    }

    public boolean isMatch(Class<?> type) {
        return this.type == null ? false : this.type.isAssignableFrom(type);
    }

    public String getCode() {
        return code;
    }

    public Class<?> getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

}
