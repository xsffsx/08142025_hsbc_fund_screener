
package com.hhhh.group.secwealth.mktdata.fund.criteria.util;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.criteria.SearchMinMaxCriteriaValue;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchRangeCriteriaValue;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;


@Component("rangeCriteriaUtil")
public class RangeCriteriaUtil extends CriteriaUtil {

    public String toString(final List<SearchRangeCriteriaValue> rangeCriterias, final String tableAlias, final String countryCode,
        final String groupMember) {
        StringBuilder criteriaBuilder = new StringBuilder();
        for (SearchRangeCriteriaValue eachRangeCriteriaItem : rangeCriterias) {
            if (criteriaBuilder.length() > 1) {
                criteriaBuilder.append(" and ");
            }
            String mappedCriteriaKey = this.getMappedCriteriaKey(eachRangeCriteriaItem.getCriteriaKey(), countryCode, groupMember);
            StringBuffer rangeItemBuffer = new StringBuffer();
            String prefixColAlia = this.prefixTableAlias(tableAlias, mappedCriteriaKey);
            String criteriaKey = "";
            String operatorLeft = "";
            String operatorRight = "";
            if (eachRangeCriteriaItem.getCriteriaValues().size() > 1) {
                criteriaBuilder.append("(");
            }
            for (SearchMinMaxCriteriaValue valueRange : eachRangeCriteriaItem.getCriteriaValues()) {
                if (rangeItemBuffer.length() > 0) {
                    rangeItemBuffer.append(" or ");
                }
                criteriaKey = eachRangeCriteriaItem.getCriteriaKey();
                operatorLeft = this.constant(valueRange.getMinOperator());
                operatorRight = this.constant(valueRange.getMaxOperator());

                String minStr = "null";
                Number min = valueRange.getMinCriteriaLimit();
                if (null != min) {
                    minStr = min.toString();
                }
                String maxStr = "null";
                Number max = valueRange.getMaxCriteriaLimit();
                if (null != max) {
                    maxStr = max.toString();
                }
                String mappedMinCriteriaValue = this.getMappedCriteriaValue(criteriaKey, minStr);
                String mappedMaxCriteriaValue = this.getMappedCriteriaValue(criteriaKey, maxStr);

                if (StringUtil.isInvalid(minStr)) {
                    rangeItemBuffer.append(this.genStr("( {} {} {} )", prefixColAlia, operatorRight, mappedMaxCriteriaValue));
                } else if (StringUtil.isInvalid(maxStr)) {
                    rangeItemBuffer.append(this.genStr("( {} {} {} )", prefixColAlia, operatorLeft, mappedMinCriteriaValue));
                } else {
                    rangeItemBuffer.append(getQueryCondition(prefixColAlia, operatorLeft, operatorRight, mappedMinCriteriaValue,
                        mappedMaxCriteriaValue));
                }
            }
            criteriaBuilder.append(rangeItemBuffer);
            if (eachRangeCriteriaItem.getCriteriaValues().size() > 1) {
                criteriaBuilder.append(")");
            }
        }
        LogUtil.debug(RangeCriteriaUtil.class, "RangeCriteria {}" + criteriaBuilder.toString());
        return criteriaBuilder.toString();
    }

    public String constant(final String s) {
        String operator = null;
        if (s.equals("gt")) {
            operator = ">";
        } else if (s.equals("ge")) {
            operator = ">=";
        } else if (s.equals("lt")) {
            operator = "<";
        } else if (s.equals("le")) {
            operator = "<=";
        }
        return operator;
    }

    private String getQueryCondition(final String prefixColAlia, final String operatorLeft, final String operatorRight,
        final String mappedMinCriteriaValue, final String mappedMaxCriteriaValue) {
        if ("tblMarketData.bidPrice".equals(prefixColAlia) && (Integer.parseInt(mappedMinCriteriaValue) == 0)) {
            return this.genStr("({} {} {})", prefixColAlia, operatorRight, mappedMaxCriteriaValue);
        }
        return this.genStr("( ({} {} {}) and ({} {} {}) )", prefixColAlia, operatorLeft, mappedMinCriteriaValue, prefixColAlia,
            operatorRight, mappedMaxCriteriaValue);
    }

}
