package org.qfox.jestful.validation;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Plugin;
import org.qfox.jestful.core.exception.BeanConfigException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年5月10日 下午12:22:18
 * @since 1.0.0
 */
public class ResultValidationPlugin implements Plugin {
    private ValidatorFactory validatorFactory;

    public ResultValidationPlugin() {
        super();
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
    }

    public ResultValidationPlugin(ValidatorFactory validatorFactory) {
        super();
        this.validatorFactory = validatorFactory;
    }

    public void config(Map<String, String> arguments) throws BeanConfigException {

    }

    public Object react(Action action) throws Exception {
        Object value = action.execute();
        Object target = action.getResource().getController();
        Method method = action.getMapping().getMethod();
        Object result = action.getResult().getValue();
        ExecutableValidator validator = validatorFactory.getValidator().forExecutables();
        Set<ConstraintViolation<Object>> violations = validator.validateReturnValue(target, method, result);

        if (violations != null && !violations.isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (ConstraintViolation<Object> violation : violations) {
                message.append(violation.getPropertyPath()).append(" : ").append(violation.getMessage()).append(" ; ");
            }
            throw new ValidationException(message.toString());
        }

        return value;
    }

    public ValidatorFactory getValidatorFactory() {
        return validatorFactory;
    }

    public void setValidatorFactory(ValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
    }

}
