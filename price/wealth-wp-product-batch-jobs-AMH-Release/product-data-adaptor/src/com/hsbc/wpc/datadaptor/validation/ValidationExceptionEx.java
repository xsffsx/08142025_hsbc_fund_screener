package com.dummy.wpc.datadaptor.validation;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.validation.BeanPropertyBindingResult;

public class ValidationExceptionEx extends ValidationException {
	private BeanPropertyBindingResult errors;
	/**
	 * Create a new {@link ValidationException} based on a message and another exception.
	 * 
	 * @param message the message for this exception
	 * @param cause the other exception
	 */
	public ValidationExceptionEx(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create a new {@link ValidationException} based on a message.
	 * 
	 * @param message the message for this exception
	 */
	public ValidationExceptionEx(String message) {
		super(message);
	}
	
	public ValidationExceptionEx(String message, BeanPropertyBindingResult errors){
		super(message);
		this.errors = errors;
	}

	public BeanPropertyBindingResult getErrors() {
		return errors;
	}

	public void setErrors(BeanPropertyBindingResult errors) {
		this.errors = errors;
	}
}
