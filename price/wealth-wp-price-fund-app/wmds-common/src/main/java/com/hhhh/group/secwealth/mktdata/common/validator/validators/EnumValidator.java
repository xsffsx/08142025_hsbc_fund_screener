/*
 */

package com.hhhh.group.secwealth.mktdata.common.validator.validators;

import java.util.ArrayList;
import java.util.Arrays;
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
 * The Class EnumValidator.
 */
public class EnumValidator extends RuleSet implements Validator {

    private static final long serialVersionUID = 7877417866906326829L;

    /** The field name. */
    private String fieldName;

    /** The enum values. */
    private String[] enumValues;

    private String[] fieldValues;

    @Override
    public void preValidate(final Object fields) throws Exception {
        String[] fieldArray = ((String) fields).split(CommonConstants.SYMBOL_SEPARATOR);
        this.fieldName = fieldArray[0];
        this.enumValues = fieldArray[1].split(CommonConstants.SYMBOL_COMMA);
    }

    @Override
    public List<ValidatorError> validate(final Map<String, String> headerMap, final JSONObject json) throws Exception {
        List<ValidatorError> validatorList = new ArrayList<ValidatorError>();
        String fieldValue = null;
        Object obj = json.get(this.fieldName);
        if (obj != null) {
            fieldValue = obj.toString();
        }
        if (StringUtil.isValid(fieldValue)) {
            this.fieldValues = fieldValue.split(CommonConstants.SYMBOL_COMMA);
            List<String> listFieldValues = Arrays.asList(this.fieldValues);
            List<String> listEnumValues = Arrays.asList(this.enumValues);
            if (listEnumValues.containsAll(listFieldValues)) {
                return validatorList;
            }
            validatorList.add(new ValidatorError(ErrTypeConstants.ENUM_INVALID, this.fieldName, "The value" + listFieldValues
                + " of field[" + this.fieldName + "] is NOT in " + listEnumValues + ".", this.getClass().getSimpleName()));
        }
        return validatorList;
    }
}