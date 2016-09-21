package org.qfox.jestful.cookie;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Plugin;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.exception.PluginConfigException;

import java.net.CookieHandler;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年5月10日 下午7:58:29
 * @since 1.0.0
 */
public class ResponseCookiePlugin implements Plugin {
    private CookieHandler cookieHandler;

    public ResponseCookiePlugin() {
        super();
        if (CookieHandler.getDefault() == null) {
            try {
                Class<?> clazz = Class.forName("java.net.CookieManager");
                this.cookieHandler = (CookieHandler) clazz.newInstance();
                CookieHandler.setDefault(cookieHandler);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("please use JVM with version 1.6 at least");
            } catch (Exception e) {
                // impossible
                throw new RuntimeException(e);
            }
        }
    }

    public ResponseCookiePlugin(CookieHandler cookieHandler) {
        super();
        this.cookieHandler = cookieHandler;
    }

    public void config(Map<String, String> arguments) throws PluginConfigException {

    }

    public Object react(Action action) throws Exception {
        Object value = action.execute();
        Response response = action.getResponse();
        URL url = new URL(action.getURL());
        URI uri = url.toURI();
        Map<String, List<String>> header = new HashMap<String, List<String>>();
        String[] keys = response.getHeaderKeys();
        for (String key : keys) {
            String[] values = response.getResponseHeaders(key);
            header.put(key, Arrays.asList(values));
        }
        cookieHandler.put(uri, header);
        return value;
    }

    public CookieHandler getCookieHandler() {
        return cookieHandler;
    }

    public void setCookieHandler(CookieHandler cookieHandler) {
        this.cookieHandler = cookieHandler;
    }

}
