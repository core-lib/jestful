package org.qfox.jestful.client.auth;

import org.qfox.jestful.core.Action;

/**
 * Created by yangchangpei on 17/10/25.
 */
public interface SchemePreference {

    /**
     * 有可能服务端发过来多个可被支持的认证方式 应当优先选择认证安全性而且当前客户端支持的认证方案, 所以应当配合用户设置的认证偏好设定来匹配
     *
     * @param schemeRegistry
     * @param action
     * @param thrown
     * @param result
     * @param exception
     * @return
     */
    Scheme matches(SchemeRegistry schemeRegistry, Action action, boolean thrown, Object result, Exception exception);

}
