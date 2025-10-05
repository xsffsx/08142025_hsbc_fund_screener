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
import com.hhhh.group.secwealth.mktdata.common.validator.vo.RuleSet;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.Validator;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.ValidatorError;

/**
 * 
 * <p>
 * <b> The validator for field's length checking. </b>
 * </p>
 */
public class FieldSizeValidator extends RuleSet implements Validator {

	private static final long serialVersionUID = 8626828872404633312L;

	private int[] minSize;

    private int[] maxSize;

    private String[] fieldArray;

    @Override
    public void preValidate(final Object obj) throws Exception {
        String[] fields = ((String) obj).split(CommonConstants.SYMBOL_SEPARATOR);
        this.minSize = new int[fields.length];
        this.fieldArray = new String[fields.length];
        this.maxSize = new int[fields.length];
        for (int i = 0; i < fields.length; i++) {
            String[] array = fields[i].split(CommonConstants.SYMBOL_COLON);
            this.minSize[i] = Integer.parseInt(array[0]);
            this.fieldArray[i] = array[1];
            this.maxSize[i] = Integer.parseInt(array[2]);
        }
    }

    @Override
    public List<ValidatorError> validate(final Map<String, String> headerMap, final JSONObject json) throws Exception {
        List<ValidatorError> validatorList = new ArrayList<ValidatorError>();
        for (int i = 0; i < this.fieldArray.length; i++) {
            Object value = json.get(this.fieldArray[i]);
            if (null == value) {
                validatorList.add(new ValidatorError(ErrTypeConstants.FIELD_SIZE_INVALID, this.fieldArray[i],
                    "The value of the parameter [" + this.fieldArray[i] + "] is invalid", this.getClass().getSimpleName()));
            } else {
                if (value instanceof JSONNull) {
                    validatorList.add(new ValidatorError(ErrTypeConstants.FIELD_SIZE_INVALID, this.fieldArray[i],
                        "The value of the parameter [" + this.fieldArray[i] + "] is invalid", this.getClass().getSimpleName()));
                } else {
                    int size = ((JSONArray) value).size();
                    if (size < this.minSize[i] || size > this.maxSize[i]) {
                        validatorList
                            .add(new ValidatorError(ErrTypeConstants.FIELD_SIZE_INVALID, this.fieldArray[i],
                                "The size of the parameter [" + this.fieldArray[i] + "] is invalid", this.getClass()
                                    .getSimpleName()));
                    }
                }
            }
        }
        return validatorList;
    }

}
