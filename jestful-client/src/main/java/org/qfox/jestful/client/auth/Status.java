package org.qfox.jestful.client.auth;

public enum Status {

    /**
     * 未收到认证挑战
     */
    UNCHALLENGED,
    /**
     * 收到认证挑战
     */
    CHALLENGED,
    /**
     * 认证失败
     */
    UNAUTHENTICATED,
    /**
     * 认证成功
     */
    AUTHENTICATED

}
