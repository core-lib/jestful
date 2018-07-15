package org.qfox.jestful.core;

/**
 * <p>
 * Description: 定义参数序列化后的所在位置,
 * 由于采用了位运算来提升可组合的可能性，
 * 框架规定[0, 10]二进制位作为通用的参数绑定域所在位置，
 * 而且（10， 20] 作为服务端保留的二进制位，
 * 剩下的（20， 31] 作为第三方拓展插件的使用二进制位。
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
     * 路径矩阵变量
     */
    int MATRIX = 1 << 6;

}