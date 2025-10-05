/*
 */
package com.hhhh.group.secwealth.mktdata.common.validator.vo;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * Validator is an interface describing a Java class that can perform Object
 * validation. The object which is validated by this method can have various
 * type, and this class has a method to confirm that whether it can validate a
 * type or not.
 * 
 */
public interface Validator {

    public void preValidate(Object obj) throws Exception;

    public List<ValidatorError> validate(Map<String, String> headerMap, JSONObject json) throws Exception;
}
