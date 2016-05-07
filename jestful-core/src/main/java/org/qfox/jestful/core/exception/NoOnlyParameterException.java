package org.qfox.jestful.core.exception;  

import org.qfox.jestful.core.Parameters;

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
 * @date 2016年5月7日 下午12:15:56
 *
 * @since 1.0.0 
 */
public class NoOnlyParameterException extends JestfulRuntimeException {
	private static final long serialVersionUID = 866741990379952691L;
	
	private final Parameters parameters;
	private final Class<?> klass;

	public NoOnlyParameterException(Parameters parameters, Class<?> klass) {
		super(klass.getName());
		this.parameters = parameters;
		this.klass = klass;
	}

	public Parameters getParameters() {
		return parameters;
	}

	public Class<?> getKlass() {
		return klass;
	}

}
 