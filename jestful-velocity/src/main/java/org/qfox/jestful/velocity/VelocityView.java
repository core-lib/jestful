package org.qfox.jestful.velocity;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Configurable;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.exception.BeanConfigException;
import org.qfox.jestful.server.View;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

/**
 * Created by yangchangpei on 17/5/17.
 */
public class VelocityView implements View, Configurable {
    private final VelocityEngine engine = new VelocityEngine();

    @Override
    public boolean supports(Action action, String extension) {
        return ".vm".equalsIgnoreCase(extension);
    }

    @Override
    public void render(ServletContext context, String path, Action action, Request request, Response response) throws Exception {
        Template template = engine.getTemplate(path);
        VelocityContext vc = new VelocityContext();
        ServletRequest req = (ServletRequest) request;
        Enumeration<String> names = req.getAttributeNames();
        while (names != null && names.hasMoreElements()) {
            String name = names.nextElement();
            Object value = req.getAttribute(name);
            vc.put(name, value);
        }
        switch (action.getDispatcher()) {
            case INCLUDE: {
                Writer writer = response.getResponseWriter();
                template.merge(vc, writer);
                writer.flush();
            }
            break;
            default: {
                OutputStream out = response.getResponseOutputStream();
                Writer writer = new OutputStreamWriter(out);
                template.merge(vc, writer);
                writer.flush();
                IOKit.close(writer);
            }
            break;
        }
    }

    @Override
    public void config(Map<String, String> arguments) throws BeanConfigException {
        Properties properties = new Properties();
        for (Map.Entry<String, String> entry : arguments.entrySet()) {
            if (!entry.getKey().startsWith("velocity.")) continue;
            String key = entry.getKey().substring("velocity.".length());
            String value = entry.getValue();
            properties.put(key, value);
        }
        engine.init(properties);
    }

}
