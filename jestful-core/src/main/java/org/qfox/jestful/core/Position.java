package org.qfox.jestful.core;

/**
 * <p>
 * Description: 定义参数序列化后的所在位置
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月12日 上午10:39:41
 * @since 1.0.0
 */
public interface Position {

    /**
     * 未知位置
     */
    int UNKNOWN = 1;
    /**
     * 请求头
     */
    int HEADER = 1 << 1;
    /**
     * 请求路径
     */
    int PATH = 1 << 2;
    /**
     * 查询字符串
     */
    int QUERY = 1 << 3;
    /**
     * 请求体
     */
    int BODY = 1 << 4;
    /**
     * Cookie
     */
    int COOKIE = 1 << 5;
    /**
     * 会话
     */
    int SESSION = 1 << 6;

}