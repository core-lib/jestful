package org.qfox.jestful.validation;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Plugin;
import org.qfox.jestful.core.exception.BeanConfigException;

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
 * @date 2016年5月10日 下午12:16:55
 *
 * @since 1.0.0
 */
public class ParameterValidationPlugin implements Plugin {
	private ValidatorFactory validatorFactory;

	public ParameterValidationPlugin() {
		super();
		this.validatorFactory = Validation.buildDefaultValidatorFactory();
	}

	public ParameterValidationPlugin(ValidatorFactory validatorFactory) {
		super();
		this.validatorFactory = validatorFactory;
	}

	public void config(Map<String, String> arguments) throws BeanConfigException {

	}

	public Object react(Action action) throws Exception {
		Object target = action.getResource().getController();
		Method method = action.getMapping().getMethod();
		Object[] parameters = action.getParameters().arguments();

		// 校验参数
		ExecutableValidator validator = validatorFactory.getValidator().forExecutables();
		Set<ConstraintViolation<Object>> violations = validator.validateParameters(target, method, parameters != null ? parameters : new Object[0]);

		// 如果校验失败
		if (violations != null && !violations.isEmpty()) {
			StringBuilder message = new StringBuilder();
			for (ConstraintViolation<Object> violation : violations) {
				message.append(violation.getPropertyPath()).append(" : ").append(violation.getMessage()).append(" ; ");
			}
			throw new ValidationException(message.toString());
		}

		return action.execute();
	}

	public ValidatorFactory getValidatorFactory() {
		return validatorFactory;
	}

	public void setValidatorFactory(ValidatorFactory validatorFactory) {
		this.validatorFactory = validatorFactory;
	}

}
