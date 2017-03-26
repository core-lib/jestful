package org.qfox.jestful.cookie;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Plugin;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.exception.PluginConfigException;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URI;
import java.net.URL;
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
 * @date 2016年5月10日 下午7:58:10
 * @since 1.0.0
 */
public class RequestCookiePlugin implements Plugin {
    private CookieHandler cookieHandler;

    public RequestCookiePlugin() {
        super();
        if (CookieHandler.getDefault() == null) {
            this.cookieHandler = new CookieManager();
            CookieHandler.setDefault(cookieHandler);
        }
        this.cookieHandler = CookieHandler.getDefault();
    }

    public RequestCookiePlugin(CookieHandler cookieHandler) {
        super();
        this.cookieHandler = cookieHandler;
    }

    public void config(Map<String, String> arguments) throws PluginConfigException {

    }

    public Object react(Action action) throws Exception {
        Request request = action.getRequest();
        URL url = new URL(action.getURL());
        URI uri = url.toURI();
        Request target = new CookieManagedRequest(request, uri, cookieHandler);
        action.setRequest(target);
        return action.execute();
    }

    public CookieHandler getCookieHandler() {
        return cookieHandler;
    }

    public void setCookieHandler(CookieHandler cookieHandler) {
        this.cookieHandler = cookieHandler;
    }

}
