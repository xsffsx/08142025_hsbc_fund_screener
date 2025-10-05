/*
 */

package com.hhhh.group.secwealth.mktdata.common.validator.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.RuleSet;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.Validator;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.ValidatorError;

/**
 * 
 * <p>
 * <b> The validator for mandatory fields checking. </b>
 * </p>
 */
public class MandatoryFieldsValidator extends RuleSet implements Validator {

    private static final long serialVersionUID = -6857655503757556272L;

    private String[] fieldArray;

    @Override
    public void preValidate(final Object obj) throws Exception {
        this.fieldArray = ((String) obj).split(CommonConstants.SYMBOL_SEPARATOR);
    }

    @Override
    public List<ValidatorError> validate(final Map<String, String> headerMap, final JSONObject json) throws Exception {
        List<ValidatorError> validatorList = new ArrayList<ValidatorError>();
        for (String field : this.fieldArray) {
            Object value = json.get(field);
            if (null == value) {
                validatorList.add(new ValidatorError(ErrTypeConstants.MANDATORY_FIELDS_INVALID, field, "The parameter " + field
                    + " is invalid", this.getClass().getSimpleName()));
            } else {
                if (value instanceof JSONNull) {
                    validatorList.add(new ValidatorError(ErrTypeConstants.MANDATORY_FIELDS_INVALID, field, "The parameter " + field
                        + " is invalid", this.getClass().getSimpleName()));
                }
                if (value instanceof String && StringUtil.isInvalid((String) value)) {
                    validatorList.add(new ValidatorError(ErrTypeConstants.MANDATORY_FIELDS_INVALID, field, "The parameter " + field
                        + " is invalid", this.getClass().getSimpleName()));
                } else if ((value instanceof JSONArray && ((JSONArray) value).size() == 0)
                    || CommonConstants.JSONARRAY_STRING_NULL.trim().equals(value.toString().trim())
                    || CommonConstants.JSONARRAY_NULL.trim().equals(value.toString().trim())) {
                    validatorList.add(new ValidatorError(ErrTypeConstants.MANDATORY_FIELDS_INVALID, field, "The parameter " + field
                        + " is invalid", this.getClass().getSimpleName()));
                }
            }
        }
        return validatorList;
    }
}
