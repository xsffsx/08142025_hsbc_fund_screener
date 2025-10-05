
package com.hhhh.group.secwealth.mktdata.fund.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchMinMaxCriteriaValue;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchRangeCriteriaValue;
// import
// com.hhhh.group.secwealth.mktdata.common.criteria.SearchRangeCriteriaValue;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.criteria.Constants;
import com.hhhh.group.secwealth.mktdata.fund.criteria.FundCriteriaKeyMapper;



@Component("baseValidateConverter")
public class BaseValidateConverter {

    // Example: [_`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~]
    private final String SPECIALCHAR_REGEX = "[><=*']|order by|ORDER BY|chr\\(|CHR\\(";

    private Pattern pattern = Pattern.compile(this.SPECIALCHAR_REGEX);

    @Resource(name = "fundCriteriaKeyMapper")
    protected FundCriteriaKeyMapper criteriaKeyMapper;

    protected static List<String> operators = new ArrayList<String>() {

        private static final long serialVersionUID = 5675224814383604122L;

        {
            add("gt");
            add("ge");
            add("eq");
            add("lt");
            add("le");
            add("ne");
            add("in");
        }
    };

    protected static final List<String> EXCLUDE_LIST = Arrays.asList(new String[] {Constants.CRITERION_REQUEST_SWITCH_OUT_FUND,
        Constants.CRITERION_REQUEST_MONTHLY_INVESTMENT_PLAN, Constants.CRITERION_REQUEST_hhhh_RISK_LEVEL_RATING,
        Constants.CRITERION_KEYWORD, Constants.hhhh_BEST_SELLER, Constants.hhhh_NEW_FUND, Constants.hhhh_RETIREMENT_FUND, Constants.hhhh_TOP5_PERFORMERS});

    // validate List[{criteriaKey,criteriaValue,operator}]
    public void validateForSearchCriteria(final List<SearchCriteria> detailedCriterias, final String countryCode,
        final String groupMember) {
        if (ListUtil.isValid(detailedCriterias)) {
            for (SearchCriteria searchCriteria : detailedCriterias) {
                validateCriteriaKey(searchCriteria.getCriteriaKey(), countryCode, groupMember);
                validateForOperator(searchCriteria.getOperator());
                validateCriteriaValue(searchCriteria.getCriteriaKey(), searchCriteria.getCriteriaValue());
            }
        }
    }

    // validate
    // List[{criteriaKey,[{minOperator,minCriteriaLimit,maxOperator,maxCriteriaLimit}]}]
    public void validateForSearchRangeCriteriaValue(final List<SearchRangeCriteriaValue> rangeCriterias, final String countryCode,
        final String groupMember) {
        if (ListUtil.isValid(rangeCriterias)) {
            for (SearchRangeCriteriaValue searchRangeCriteriaValue : rangeCriterias) {
                if (null != searchRangeCriteriaValue) {
                    validateCriteriaKey(searchRangeCriteriaValue.getCriteriaKey(), countryCode, groupMember);
                    List<SearchMinMaxCriteriaValue> criteriaValues = searchRangeCriteriaValue.getCriteriaValues();
                    if (ListUtil.isValid(criteriaValues)) {
                        for (SearchMinMaxCriteriaValue searchMinMaxCriteriaValue : criteriaValues) {
                            validateForOperator(searchMinMaxCriteriaValue.getMinOperator());
                            validateForOperator(searchMinMaxCriteriaValue.getMaxOperator());
                            validateMinMaxCriteriaLimit(searchMinMaxCriteriaValue.getMinCriteriaLimit(),
                                searchMinMaxCriteriaValue.getMaxCriteriaLimit());
                        }
                    } else {
                        LogUtil.error(BaseValidateConverter.class, "search MinMaxCriteriaValue is invalid");
                        throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
                    }
                }
            }
        }
    }

    // validate operator
    // cant be null and must be in operators
    protected void validateForOperator(final String operator) {
        if (StringUtil.isValid(operator)) {
            if (!BaseValidateConverter.operators.contains(operator)) {
                LogUtil.error(BaseValidateConverter.class, " criteria's operator is invalid, the operator is : " + operator);
                throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
            }
        } else {
            LogUtil.error(BaseValidateConverter.class, "criteria's operator is invalid");
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        }
    }

    // validate criteria key
    // can be mapped by fundCriteriaKeyMapper
    protected void validateCriteriaKey(final String criteriaKey, final String countryCode, final String groupMember) {
        if (StringUtil.isValid(criteriaKey)) {
            if (this.criteriaKeyMapper != null
                && this.criteriaKeyMapper.getDbFieldName(criteriaKey, countryCode, groupMember) == null) {
                // check exclude list(switchoutFund, allowSellMipProdInd, HRR)
                if (!BaseValidateConverter.EXCLUDE_LIST.contains(criteriaKey)) {
                    LogUtil.error(BaseValidateConverter.class,
                        "criteria key can not be find in FundCriteriaKeyMapper, the key is : " + criteriaKey);
                    throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
                }
            }
        } else {
            LogUtil.error(BaseValidateConverter.class, "criteria key is null");
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        }
    }

    // validate range value
    // max value must be more than min value
    protected void validateMinMaxCriteriaLimit(final Number min, final Number max) {
        if (null == min && null == max) {
            LogUtil.error(BaseValidateConverter.class, "maxCriteriaLimit and minCriteriaLimit is invalid");
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        }
        if (null != min && null != max) {
            if (Double.valueOf(max + "") < Double.valueOf(min + "")) {
                LogUtil.error(BaseValidateConverter.class,
                    " maxCriteriaLimit little than minCriteriaLimit, the max and min is : " + max + min);
                throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
            }
        }
    }

    protected void validateCriteriaValue(final String criteriaKey, final String criteriaValue) {
        if (StringUtil.isValid(criteriaKey) && StringUtil.isValid(criteriaValue)) {
            if (isSpecialChar(criteriaValue)) {
                LogUtil.error(BaseValidateConverter.class, "criteriaValue include special char, criteria value=", criteriaValue);
                throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
            }
        }
    }

    private boolean isSpecialChar(final String str) {
        Matcher m = this.pattern.matcher(str);
        return m.find();
    }
}
