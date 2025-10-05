package com.dummy.wpc.datadaptor.validation;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;

public class SpringValidatorEx implements Validator, InitializingBean {
	private org.springframework.validation.Validator validator;

	/**
	 * @see Validator#validate(Object)
	 */
	public void validate(Object item) throws ValidationException {

		if (!validator.supports(item.getClass())) {
			throw new ValidationException("Validation failed for " + item + ": " + item.getClass().getName() + " class is not supported by validator.");
		}

		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(item, "item");

		validator.validate(item, errors);

		if (errors.hasErrors()) {
			throw new ValidationExceptionEx("Validation failed for " + item, errors);
		}
	}

	public void setValidator(org.springframework.validation.Validator validator) {
		this.validator = validator;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(validator, "validator must be set");
	}
}
