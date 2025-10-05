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
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.RuleSet;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.Validator;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.ValidatorError;

/**
 * 
 * <p>
 * <b> The validator for mandatory list fields checking. </b>
 * </p>
 */
public class MandatoryListFieldsValidator extends RuleSet implements Validator {

    private static final long serialVersionUID = -7464942005660763425L;

    private String[] expressionArray;

    @Override
    public void preValidate(final Object obj) throws Exception {
        this.expressionArray = ((String) obj).split(CommonConstants.SYMBOL_SEPARATOR);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public List<ValidatorError> validate(final Map<String, String> headerMap, final JSONObject json) throws Exception {

        List<ValidatorError> errorList = new ArrayList<ValidatorError>();
        for (String expression : this.expressionArray) {
            String[] param = expression.split(CommonConstants.SYMBOL_COLON);
            String reqParam = param[0];
            String[] reqParamFiles = param[1].split(CommonConstants.SYMBOL_COMMA);
            List<Map<String, Object>> filedMaps = (List<Map<String, Object>>) json.get(reqParam);
            if (null != filedMaps) {
                for (Map<String, Object> filedMap : filedMaps) {
                    for (String field : reqParamFiles) {
                        Object value = filedMap.get(field);
                        if (null == value) {
                            errorList.add(new ValidatorError(ErrTypeConstants.MANDATORY_LIST_FIELDS_INVALID, field,
                                "The parameter " + field + " is invalid", this.getClass().getSimpleName()));
                            continue;
                        }
                        if (value instanceof JSONNull) {
                            errorList.add(new ValidatorError(ErrTypeConstants.MANDATORY_LIST_FIELDS_INVALID, field,
                                "The parameter " + field + " is invalid", this.getClass().getSimpleName()));
                            continue;
                        }
                        if (value instanceof String && StringUtil.isInvalid((String) value)) {
                            errorList.add(new ValidatorError(ErrTypeConstants.MANDATORY_LIST_FIELDS_INVALID, field,
                                "The parameter " + field + " is invalid", this.getClass().getSimpleName()));
                        } else {
                            if (value instanceof List && !ListUtil.isValid((List) value)) {
                                errorList.add(new ValidatorError(ErrTypeConstants.MANDATORY_LIST_FIELDS_INVALID, field,
                                    "The parameter " + field + " is invalid", this.getClass().getSimpleName()));
                            }
                        }
                    }
                }
            }
        }
        return errorList;
    }

}
