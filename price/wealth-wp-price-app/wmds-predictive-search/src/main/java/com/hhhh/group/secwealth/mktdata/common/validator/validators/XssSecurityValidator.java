/*
 */

package com.hhhh.group.secwealth.mktdata.common.validator.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.RuleSet;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.Validator;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.ValidatorError;

/**
 * 
 * <p>
 * <b> The validator for mandatory fields checking. </b>
 * </p>
 */
public class XssSecurityValidator extends RuleSet implements Validator {

    private static final long serialVersionUID = -1054455991154345892L;

    private String[] fieldArray;

    @Override
    public void preValidate(final Object obj) throws Exception {
        this.fieldArray = ((String) obj).split(CommonConstants.SYMBOL_SEPARATOR);
    }

    @Override
    public List<ValidatorError> validate(final Map<String, String> headerMap, final JSONObject json) throws Exception {

        List<ValidatorError> validatorList = new ArrayList<ValidatorError>();
        StringBuffer buffer = new StringBuffer();
        for (String field : this.fieldArray) {
            Object value = json.get(field);
            if (null != value) {
                if (value instanceof String) {
                    buffer.append(value);
                } else {
                    buffer.append(json.get(field + CommonConstants.ORIGINAL_FIELD_SUFFIX));
                }
            }
        }
        if (!XssValidator.getInstance().isSafe(buffer.toString())) {
            validatorList.add(new ValidatorError(ErrTypeConstants.XSS_SECURITY_INVALID, buffer.toString(), "The parameter String "
                + buffer.toString() + " is invalid", this.getClass().getSimpleName()));
        }
        return validatorList;
    }

    public String[] getFieldArray() {
        return this.fieldArray;
    }

    public void setFieldArray(final String[] fieldArray) {
        this.fieldArray = fieldArray;
    }

}
