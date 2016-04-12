package org.qfox.jestful.core;

/**
 * 
 * <p>
 * Description: 定义返回结果序列化后的所在位置
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
public enum Location {

	/**
	 * 状态行
	 */
	STATUS,
	/**
	 * 请求头
	 */
	HEADER,
	/**
	 * 请求体
	 */
	BODY;

}