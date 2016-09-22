package org.qfox.jestful.server.webxml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangchangpei on 16/9/21.
 */
public class ErrorConfig {
    private final List<ErrorPage> errorPages;

    public ErrorConfig(Document document) throws ClassNotFoundException {
        this.errorPages = new ArrayList<ErrorPage>();
        NodeList pages = document.getElementsByTagName("error-page");
        for (int i = 0; i < pages.getLength(); i++) {
            ErrorPage page = new ErrorPage((Element) pages.item(i));
            this.errorPages.add(page);
        }
    }

    public String getLocationByCode(int code) {
        for (ErrorPage page : errorPages) {
            if (page.isMatch(code)) {
                return page.getLocation();
            }
        }
        return null;
    }

    public String getLocationByType(Class<?> type) {
        for (ErrorPage page : errorPages) {
            if (page.isMatch(type)) {
                return page.getLocation();
            }
        }
        return null;
    }

    public List<ErrorPage> getErrorPages() {
        return errorPages;
    }

}
