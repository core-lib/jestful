package org.qfox.jestful.core;

/**
 * 
 * <p>
 * Description: 定义参数序列化后的所在位置
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月12日 上午10:39:41
 *
 * @since 1.0.0
 */
public enum Position {

	/**
	 * 请求头
	 */
	HEADER,
	/**
	 * 请求路径
	 */
	PATH,
	/**
	 * 查询字符串
	 */
	QUERY,
	/**
	 * 请求体
	 */
	BODY;

}