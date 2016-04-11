package org.qfox.jestful.commons.tree;


/**
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月1日 下午7:21:49
 *
 * @since 1.0.0
 */
public class AlreadyValuedException extends IllegalStateException {
	private static final long serialVersionUID = -6954428631812725990L;

	private final Node<?, ?> current;
	private final Node<?, ?> existed;

	public AlreadyValuedException(Node<?, ?> current, Node<?, ?> existed) {
		super();
		this.current = current;
		this.existed = existed;
	}

	public Node<?, ?> getCurrent() {
		return current;
	}

	public Node<?, ?> getExisted() {
		return existed;
	}

}
