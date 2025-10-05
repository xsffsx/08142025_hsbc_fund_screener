/*
 */

package com.hhhh.group.secwealth.mktdata.common.validator.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.RuleSet;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.Validator;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.ValidatorError;

/**
 * 
 * <p>
 * <b> The validator for Equals checking. </b>
 * </p>
 */
public class EqualsValidator extends RuleSet implements Validator {

    private static final long serialVersionUID = 5756297304327990989L;

    private String fieldName;

    private String compareValue;

    @Override
    public void preValidate(final Object obj) throws Exception {
        String[] fieldArray = ((String) obj).split(CommonConstants.SYMBOL_SEPARATOR);
        this.fieldName = fieldArray[0];
        this.compareValue = fieldArray[1];
    }

    @Override
    public List<ValidatorError> validate(final Map<String, String> headerMap, final JSONObject json) throws Exception {

        List<ValidatorError> validatorList = new ArrayList<ValidatorError>();
        Object fieldValue = json.get(this.fieldName);
        if (fieldValue instanceof JSONNull) {
            validatorList.add(new ValidatorError(ErrTypeConstants.ENUM_INVALID, this.compareValue,
                "The field is null or isn't equal " + this.compareValue + ")", this.getClass().getSimpleName()));
        }
        if (null == fieldValue || !fieldValue.equals(this.compareValue)) {
            validatorList.add(new ValidatorError(ErrTypeConstants.ENUM_INVALID, this.compareValue,
                "The field is null or isn't equal " + this.compareValue + ")", this.getClass().getSimpleName()));
        }


        return validatorList;
    }
}
