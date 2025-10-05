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
 * The Class IntegerValidator.
 */
public class IntegerValidator extends RuleSet implements Validator {

    private static final long serialVersionUID = 3491784870736733085L;
    /** The field array. */
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
            if (StringUtil.isValid(value.toString())) {
                try {
                    json.put(field, Integer.valueOf(value.toString()));
                } catch (Exception e) {
                    validatorList.add(new ValidatorError(ErrTypeConstants.INTEGER_INVALID, field, "The parameter " + field
                        + " is not an integer.", this.getClass().getSimpleName()));
                }
            } else {
                json.put(field, null);
            }
        }
        return validatorList;
    }

    /**
     * Gets the field array.
     * 
     * @return the field array
     */
    public String[] getFieldArray() {
        return this.fieldArray;
    }

    /**
     * Sets the field array.
     * 
     * @param fieldArray
     *            the new field array
     */
    public void setFieldArray(final String[] fieldArray) {
        this.fieldArray = fieldArray;
    }

}
