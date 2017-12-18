package org.qfox.jestful.client.cache;

import org.qfox.jestful.core.Status;

import java.io.InputStream;
import java.util.Map;

/**
 * 回应缓存对象
 */
public interface Cache {

    /**
     * @return 是否足够保鲜 即不用经过协商也可以直接使用
     */
    boolean fresh();

    /**
     * @return 是否为可协商的缓存 即缓存已过时或使用前必须和服务器协商
     */
    boolean negotiable();

    /**
     * 通过添加请求头的方式再发起请求来和服务器协商
     *
     * @param request 请求
     */
    void negotiate(NegotiatedRequest request);

    /**
     * 协商过且可以使用的
     *
     * @param response 回应
     * @return 如果与服务器协商过而且当前还是新鲜的则返回 {@code true} 否则返回{@code false}
     */
    boolean negotiated(NegotiatedResponse response);

    /**
     * @return 获取回应状态
     */
    Status getStatus();

    /**
     * @return 缓存的回应头
     */
    Map<String, String[]> getHeader();

    /**
     * @return 缓存的回应体
     */
    InputStream getBody();

}
