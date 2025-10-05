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
 * <p>
 * <b>Mandatory Checking for Market if ProductAltClassCode = M</b>
 * </p>
 */
public class MandatoryWithAssociateFieldsValidator extends RuleSet implements Validator {

    private static final long serialVersionUID = -7039787722969884500L;

    private String[] fieldArray;
    private String[] fieldValues;
    private String[] mandatoryFields;
    private String fieldName;

    @Override
    public void preValidate(final Object obj) throws Exception {
        this.fieldArray = ((String) obj).split(CommonConstants.SYMBOL_SEPARATOR);
        this.fieldName = this.fieldArray[0];
        this.fieldValues = this.fieldArray[1].split(CommonConstants.SYMBOL_COMMA);

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ValidatorError> validate(final Map<String, String> headerMap, final JSONObject json) throws Exception {

        List<ValidatorError> validatorList = new ArrayList<ValidatorError>();
        if (getAssociateFields() != "" && getAssociateFields() != null) {
            this.mandatoryFields = getAssociateFields().split(CommonConstants.SYMBOL_COMMA);
        } else {
            this.mandatoryFields[0] = "market";
        }
        String paraValue = json.get(this.fieldName) == null ? null : json.get(this.fieldName).toString();

        for (String fieldValue : this.fieldValues) {
            if (fieldValue.equalsIgnoreCase(paraValue)) {
                for (String mandatoryField : this.mandatoryFields) {
                    Object value = json.get(mandatoryField);
                    if (value instanceof String && StringUtil.isInvalid((String) value)) {
                        validatorList.add(new ValidatorError(ErrTypeConstants.MANDATORY_WITHASSOCIATE_FIELDS_INVALID,
                            mandatoryField, "The parameter " + mandatoryField + " is invalid", this.getClass().getSimpleName()));
                    } else {
                        if (value instanceof List && !ListUtil.isValid((List<Object>) value)) {
                            validatorList
                                .add(new ValidatorError(ErrTypeConstants.MANDATORY_WITHASSOCIATE_FIELDS_INVALID, mandatoryField,
                                    "The parameter " + mandatoryField + " is invalid", this.getClass().getSimpleName()));
                        }
                    }
                }
            }
        }
        return validatorList;
    }

}
