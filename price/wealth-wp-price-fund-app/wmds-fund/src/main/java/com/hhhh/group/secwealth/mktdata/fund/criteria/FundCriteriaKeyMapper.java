
package com.hhhh.group.secwealth.mktdata.fund.criteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.bidimap.DualHashBidiMap;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;


public class FundCriteriaKeyMapper {

    private final static String DEFAULT_EXCHANGE = "DEFAULT";

    private List<String> textTypeCriteriaItems = new ArrayList<String>();

    private Map<String, DualHashBidiMap> criteriaKeyMapByExchange = new HashMap<String, DualHashBidiMap>();


    public FundCriteriaKeyMapper(final Map<String, Map<String, String>> map, final List<String> textTypeCriteriaItems) {
        super();

        this.textTypeCriteriaItems = textTypeCriteriaItems;
        for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
            this.criteriaKeyMapByExchange.put(entry.getKey(), new DualHashBidiMap(entry.getValue()));
        }
    }

    public String getCriteriaKey(final String exchange, final String dbFieldName) {

        DualHashBidiMap criteriaKeyMap = this.getCriteriaKeyMap(exchange);
        Object itemCode = criteriaKeyMap == null ? null : criteriaKeyMap.getKey(dbFieldName);

        if (itemCode == null && !exchange.equals(FundCriteriaKeyMapper.DEFAULT_EXCHANGE)) {
            criteriaKeyMap = this.getCriteriaKeyMap(FundCriteriaKeyMapper.DEFAULT_EXCHANGE);
            itemCode = criteriaKeyMap.getKey(dbFieldName);
        }
        return itemCode == null ? null : itemCode.toString();
    }

    public String getDbFieldName(final String criteriaKey, final String countryCode, final String groupMember) {
        // Get value by CountryCode_GroupMember
        String exchange = countryCode + CommonConstants.SYMBOL_UNDERLINE + groupMember;
        DualHashBidiMap itemCodeMap = this.getCriteriaKeyMap(exchange);
        Object itemCode = itemCodeMap == null ? null : itemCodeMap.get(criteriaKey);

        // Get value by CountryCode
        if (itemCode == null && !exchange.equals(FundCriteriaKeyMapper.DEFAULT_EXCHANGE)) {
            exchange = countryCode;
            itemCodeMap = this.getCriteriaKeyMap(exchange);
            itemCode = itemCodeMap == null ? null : itemCodeMap.get(criteriaKey);
        }
        // Get value by DEFAULT
        if (itemCode == null && !exchange.equals(FundCriteriaKeyMapper.DEFAULT_EXCHANGE)) {
            itemCodeMap = this.getCriteriaKeyMap(FundCriteriaKeyMapper.DEFAULT_EXCHANGE);
            itemCode = itemCodeMap.get(criteriaKey);
        }
        return itemCode == null ? null : itemCode.toString();
    }

    private DualHashBidiMap getCriteriaKeyMap(final String exchange) {

        DualHashBidiMap itemCodeMap = this.criteriaKeyMapByExchange.get(exchange);
        if (itemCodeMap == null) {
            itemCodeMap = this.criteriaKeyMapByExchange.get(FundCriteriaKeyMapper.DEFAULT_EXCHANGE);
        }
        return itemCodeMap;
    }


    public List<String> getTextTypeCriteriaItems() {
        return this.textTypeCriteriaItems;
    }


    public void setTextTypeCriteriaItems(final List<String> textTypeCriteriaItems) {
        this.textTypeCriteriaItems = textTypeCriteriaItems;
    }


}
