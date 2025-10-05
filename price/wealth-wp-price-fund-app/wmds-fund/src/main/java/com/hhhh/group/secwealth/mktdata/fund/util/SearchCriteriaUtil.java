
package com.hhhh.group.secwealth.mktdata.fund.util;

import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.ValidatorError;
import com.hhhh.group.secwealth.mktdata.fund.criteria.util.Operator;

import java.util.ArrayList;
import java.util.List;

public class SearchCriteriaUtil {

    public static ValidatorError validate(final SearchCriteria criteria) {
        ValidatorError error = null;
        String operator = criteria.getOperator();
        String criteriaValue = criteria.getCriteriaValue();
        if (StringUtil.isValid(criteriaValue) && !CommonConstants.ALL.equals(criteriaValue.toUpperCase())) {
            try {
                Operator.valueOf(operator.toUpperCase()).name();
            } catch (Exception e) {
                LogUtil.error(SearchCriteriaUtil.class, CommonConstants.OPERATOR, "The invalid input param is operator " + "[" + operator + "].");
                error = new ValidatorError(ErrTypeConstants.SEARCH_CRITERIA_INVALID, CommonConstants.OPERATOR,
                    "The invalid input param is operator" + "[" + operator + "].", "CriteriaUtil validate");
            }
        }
        return error;
    }

    public static List<ValidatorError> validate(final List<SearchCriteria> criterias) {
        List<ValidatorError> validatorList = null;
        if (ListUtil.isValid(criterias)) {
            validatorList = new ArrayList<ValidatorError>();
            for (SearchCriteria criteria : criterias) {
                ValidatorError error = validate(criteria);
                if (error != null) {
                    validatorList.add(error);
                }
            }
        }
        return validatorList;
    }

    public static String getDbExpression(final SearchCriteria criteria){
        String operator = criteria.getOperator();
        String criteriaValue = criteria.getCriteriaValue();
        try {
            if (StringUtil.isValid(operator) && StringUtil.isValid(criteriaValue)
                && !CommonConstants.ALL.equals(criteriaValue.toUpperCase())) {
                return Operator.valueOf(operator.toUpperCase()).getText();
            }
        } catch (Exception e) {
            LogUtil.error(SearchCriteriaUtil.class, "operator", "The invalid input param is operator" + "[" + operator + "].");
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        }
        return null;
    }

}
