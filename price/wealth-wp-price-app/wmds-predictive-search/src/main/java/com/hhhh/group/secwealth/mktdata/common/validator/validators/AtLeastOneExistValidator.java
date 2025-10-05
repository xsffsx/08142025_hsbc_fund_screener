/*
 */

package com.hhhh.group.secwealth.mktdata.common.validator.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.RuleSet;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.Validator;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.ValidatorError;

/**
 * 
 * <p>
 * <b> The validator for At Least One Exist fields checking. </b>
 * </p>
 */
public class AtLeastOneExistValidator extends RuleSet implements Validator {

    private static final long serialVersionUID = -9164711381471273768L;

    private String[] fieldArray;

    @Override
    public void preValidate(final Object obj) throws Exception {

        this.fieldArray = ((String) obj).split(CommonConstants.SYMBOL_SEPARATOR);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ValidatorError> validate(final Map<String, String> headerMap, final JSONObject json) throws Exception {

        List<ValidatorError> validatorList = new ArrayList<ValidatorError>();
        boolean flag = true;
        // New StringBuilder just for log display
        StringBuilder bf = new StringBuilder();
        for (String field : this.fieldArray) {
            bf.append(CommonConstants.SPACE).append(field).append(CommonConstants.SPACE);
            Object value = json.get(field);
            if (value instanceof String) {
                if (StringUtil.isValid((String) value)) {
                    flag = false;
                    break;
                }
            } else {
                if (value instanceof List && ListUtil.isValid((List<Object>) value)) {
                    flag = false;
                    break;
                }
            }
        }
        if (flag) {
            validatorList.add(new ValidatorError(ErrTypeConstants.ATLEAST_ONE_EXIST_INVALID, bf.toString(),
                "At least one of the parameters [ " + bf.toString() + "] should be exist ", this.getClass().getSimpleName()));
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
