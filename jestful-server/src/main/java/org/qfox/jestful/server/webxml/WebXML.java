package org.qfox.jestful.server.webxml;

import org.qfox.jestful.core.exception.JestfulRuntimeException;
import org.qfox.jestful.core.io.IOUtils;
import org.w3c.dom.Document;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by yangchangpei on 16/9/21.
 */
public class WebXML {
    private final ErrorConfig errorConfig;

    public WebXML(ServletContext context) {
        InputStream in = null;
        try {
            URL url = context.getResource("/WEB-INF/web.xml");
            in = url.openStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(in);
            this.errorConfig = new ErrorConfig(document);
        } catch (Exception e) {
            throw new JestfulRuntimeException(e);
        } finally {
            IOUtils.close(in);
        }
    }

    public ErrorConfig getErrorConfig() {
        return errorConfig;
    }

}
