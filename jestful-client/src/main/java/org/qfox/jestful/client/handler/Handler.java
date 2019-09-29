package org.qfox.jestful.client.handler;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.Action;

/**
 * 请求方法处理器
 *
 * @author Payne 646742615@qq.com
 * 2019/9/29 16:16
 */
public interface Handler {

    /**
     * 写
     *
     * @param client 客户端
     * @param action 请求
     * @throws Exception 异常
     */
    void doActionWriting(Client client, Action action) throws Exception;

    /**
     * 读
     *
     * @param client 客户端
     * @param action 请求
     * @throws Exception 异常
     */
    void doActionReading(Client client, Action action) throws Exception;

}
