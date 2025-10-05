
package com.hhhh.group.secwealth.mktdata.fund.criteria.util;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.fund.criteria.FundCriteriaKeyMapper;


@Component("criteriaUtil")
public class CriteriaUtil {

    @Resource(name = "fundCriteriaKeyMapper")
    protected FundCriteriaKeyMapper criteriaKeyMapper;

    @Resource(name = "criteriaValueMap")
    protected Map<String, Map<String, String>> criteriaValueMap;

    protected Map<String, Integer> ratingValueMap;


    protected String getMappedCriteriaKey(final String criteriaKey, final String countryCode, final String groupMember) {

        if (this.criteriaKeyMapper == null) {
            return criteriaKey;
        } else {
            return this.criteriaKeyMapper.getDbFieldName(criteriaKey, countryCode, groupMember);
        }
    }

    protected String prefixTableAlias(final String tableAlias, final String fieldName) {
        if (tableAlias == null || fieldName == null || fieldName.contains(".")) {
            return fieldName;
        } else {
            return new StringBuilder(tableAlias).append(".").append(fieldName).toString();
        }
    }

    protected String getMappedCriteriaValue(final String criteriaKey, final String criteriaValue) {
        if (this.ratingValueMap != null && this.ratingValueMap.containsKey(criteriaKey + criteriaValue)) {
            return this.ratingValueMap.get(criteriaKey + criteriaValue).toString();
        } else if (this.criteriaValueMap != null) {
            if (this.criteriaValueMap.containsKey(criteriaKey)) {
                Map<String, String> valuesMap = this.criteriaValueMap.get(criteriaKey);
                if (valuesMap != null) {
                    return valuesMap.get(criteriaValue);
                }
            }
        }
        return criteriaValue;
    }

    protected String genStr(final String source, final String... values) {
        String newSource = source;
        if (source != null) {
            if ((values != null) && (values.length > 0)) {
                for (String value : values) {
                    if (value != null) {
                        newSource = newSource.replaceFirst("\\{\\}", value);
                    }
                }
            }
        }
        return newSource;
    }

}
