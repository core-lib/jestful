package org.qfox.jestful.core.exception;

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
 * @date 2016年5月14日 上午11:10:11
 *
 * @since 1.0.0
 */
public class PluginConfigException extends JestfulRuntimeException {
	private static final long serialVersionUID = -342613651593818343L;

	public PluginConfigException() {
		super();
	}

	public PluginConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public PluginConfigException(String message) {
		super(message);
	}

	public PluginConfigException(Throwable cause) {
		super(cause);
	}

}
