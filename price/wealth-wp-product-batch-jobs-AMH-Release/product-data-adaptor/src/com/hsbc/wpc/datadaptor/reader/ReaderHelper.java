package com.dummy.wpc.datadaptor.reader;

import java.util.List;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

import com.dummy.wpc.datadaptor.validation.ValidationExceptionEx;

public class ReaderHelper {
	public static String getErrorMsg(ValidationException ex) {
		String errMsg = "";
		if(ex != null){
			if(ex instanceof ValidationExceptionEx){
				BeanPropertyBindingResult errors = ((ValidationExceptionEx) ex).getErrors();
				if(errors != null){
					List<FieldError> fieldErrorList = errors.getFieldErrors();
					if(fieldErrorList != null){
						for(int i = 0 ; i < fieldErrorList.size(); i ++){
							FieldError fieldError = fieldErrorList.get(i);
							errMsg += "    " + fieldError.getCode() + " : " + fieldError.getDefaultMessage();
							if(i < fieldErrorList.size() - 1){
								errMsg += "\r\n";
							}
						}
					}
				}
			}
			else{
				errMsg = "    " + ex.getMessage();
			}
		}
		return errMsg;
	}
}
