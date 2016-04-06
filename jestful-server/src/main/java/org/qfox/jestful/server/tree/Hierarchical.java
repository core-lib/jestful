package org.qfox.jestful.server.tree;

import org.qfox.jestful.server.exception.AlreadyValuedException;

/**
 * <p>
 * Description: 有层级关系的接口,实现该接口表明该类型和自己或其他类型有层级关系, 实现{@link Hierarchical#toNode()}来将对象转换成树的节点
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月1日 下午3:54:56
 *
 * @since 1.0.0
 */
public interface Hierarchical<K extends Expression<K>, V extends Comparable<V>> {

	/**
	 * 转换成对应的树节点
	 * 
	 * @return 对应的树节点
	 * @throws AlreadyValuedException
	 *             当需要合并的两个节点都存在值的情况下抛出,表明不能合并
	 */
	Node<K, V> toNode() throws AlreadyValuedException;

}
