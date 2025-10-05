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
 * <b> The validator for field's length checking. </b>
 * </p>
 */
public class FieldLengthValidator extends RuleSet implements Validator {

    private static final long serialVersionUID = 5379922263362180293L;

    private int[] minLengths;

    private int[] maxLengths;

    private String[] fieldArray;

    @Override
    public void preValidate(final Object obj) throws Exception {
        String[] fields = ((String) obj).split(CommonConstants.SYMBOL_SEPARATOR);
        this.minLengths = new int[fields.length];
        this.fieldArray = new String[fields.length];
        this.maxLengths = new int[fields.length];
        for (int i = 0; i < fields.length; i++) {
            String[] array = fields[i].split(CommonConstants.SYMBOL_COLON);
            this.minLengths[i] = Integer.parseInt(array[0]);
            this.fieldArray[i] = array[1];
            this.maxLengths[i] = Integer.parseInt(array[2]);
        }
    }

    @Override
    public List<ValidatorError> validate(final Map<String, String> headerMap, final JSONObject json) throws Exception {
        List<ValidatorError> validatorList = new ArrayList<ValidatorError>();
        for (int i = 0; i < this.fieldArray.length; i++) {
            Object value = json.get(this.fieldArray[i]);
            if (null == value) {
                validatorList.add(new ValidatorError(ErrTypeConstants.FIELD_LENGTH_INVALID, this.fieldArray[i],
                    "The value of the parameter [" + this.fieldArray[i] + "] is invalid", this.getClass().getSimpleName()));
            } else {
                if (value instanceof JSONNull) {
                    validatorList.add(new ValidatorError(ErrTypeConstants.FIELD_LENGTH_INVALID, this.fieldArray[i],
                        "The value of the parameter [" + this.fieldArray[i] + "] is invalid", this.getClass().getSimpleName()));
                } else {
                    int length = ((String) value).length();
                    if (length < this.minLengths[i] || length > this.maxLengths[i]) {
                        validatorList
                            .add(new ValidatorError(ErrTypeConstants.FIELD_LENGTH_INVALID, this.fieldArray[i],
                                "The length of the parameter [" + this.fieldArray[i] + "] is invalid", this.getClass()
                                    .getSimpleName()));
                    }
                }
            }
        }
        return validatorList;
    }

}
