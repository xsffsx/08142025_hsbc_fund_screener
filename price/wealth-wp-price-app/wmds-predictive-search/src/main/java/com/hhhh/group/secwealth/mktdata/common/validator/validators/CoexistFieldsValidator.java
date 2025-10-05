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
 * <b> The validator for Coexist Fields fields checking. </b>
 * </p>
 */
public class CoexistFieldsValidator extends RuleSet implements Validator {

    private static final long serialVersionUID = -1982677493431524907L;

    private String[] fieldArray;

    @Override
    public void preValidate(final Object obj) throws Exception {

        this.fieldArray = ((String) obj).split(CommonConstants.SYMBOL_SEPARATOR);
    }

    @Override
    public List<ValidatorError> validate(final Map<String, String> headerMap, final JSONObject json) throws Exception {

        List<ValidatorError> validatorList = new ArrayList<ValidatorError>();
        int flag = 0;
        for (String field : this.fieldArray) {
            Object value = json.get(field);
            int temp = flag;
            if (value instanceof String) {
                if (StringUtil.isInvalid((String) value)) {
                    // status 1 when the parameter is null
                    flag = 1;
                } else {
                    // status 2 when the parameter isn't null
                    flag = 2;
                }
            } else {
                if (!ListUtil.isValid((List<?>) value)) {
                    // status 1 when the parameter is null
                    flag = 1;
                } else {
                    // status 2 when the parameter isn't null
                    flag = 2;
                }
            }
            if (temp != 0 && temp != flag) {
                validatorList.add(new ValidatorError(ErrTypeConstants.COEXIST_FIELDS_INVALID, field, "The parameters [ " + field
                    + "]should coexist.", this.getClass().getSimpleName()));
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
