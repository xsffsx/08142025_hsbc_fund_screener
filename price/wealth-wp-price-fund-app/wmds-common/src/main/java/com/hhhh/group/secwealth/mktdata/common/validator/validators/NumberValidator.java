/*
 */
package com.hhhh.group.secwealth.mktdata.common.validator.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
 * <b> The validator for Number fields checking. </b>
 * </p>
 */
public class NumberValidator extends RuleSet implements Validator {

    private static final long serialVersionUID = -6848678835664691309L;

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
            if (StringUtil.isValid(value.toString().trim())) {
                if (!StringUtil.isNumeric(value.toString().trim())) {
                    validatorList.add(new ValidatorError(ErrTypeConstants.NUMBER_INVALID, field, "The parameter " + field
                        + " is not a number.", this.getClass().getSimpleName()));
                }
            }
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
