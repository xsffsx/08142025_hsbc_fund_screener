/*
 */
package com.hhhh.group.secwealth.mktdata.common.criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.hhhh.group.secwealth.mktdata.common.common.ConvertorKey;
import com.hhhh.group.secwealth.mktdata.common.convertor.ConvertorUtil;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;

/**
 * The Class SearchRangeCriteria.
 */
public class SearchRangeCriteria {

    /** The min. */
    private SearchCriteria min;

    /** The max. */
    private SearchCriteria max;

    /**
     * Gets the from context.
     * 
     * @param map
     *            the map
     * @param requestParam
     *            the request param
     * @return the from context
     */
    @SuppressWarnings("unchecked")
    public static List<List<SearchRangeCriteria>> getFromContext(final Map<String, Object> map, final String requestParam,
        final String convertorKey, final boolean isCheckKey) {
        List<List<SearchRangeCriteria>> criteriaList = null;
        List<String> criteriaStrList = (List<String>) map.get(requestParam);
        if (ListUtil.isValid(criteriaStrList)) {
            criteriaList = new ArrayList<List<SearchRangeCriteria>>();
            try {
                for (String c : criteriaStrList) {
                    List<SearchRangeCriteria> srcList = new ArrayList<SearchRangeCriteria>();
                    criteriaList.add(srcList);
                    String[] cArray = c.split(CommonConstants.SYMBOL_EQUAL);
                    String key = cArray[0];
                    String[] rangeStrArray = cArray[1].split(CommonConstants.SYMBOL_SEPARATOR);
                    if (null != convertorKey) {
                        String criteriaKey = (String) ConvertorUtil.doConvert(convertorKey, key);
                        if (isCheckKey && null == criteriaKey) {
                            String requestParamStr = (String) map.get(StringUtil.getOriginalName(requestParam));
                            LogUtil.error(SearchRangeCriteria.class, "The invalid input param is " + requestParam + "["
                                + requestParamStr + "].");
                            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, "The invalid input param is "
                                + requestParam + "[" + requestParamStr + "].");
                        }
                    }
                    for (String rangeStr : rangeStrArray) {
                        SearchRangeCriteria src = new SearchRangeCriteria();
                        srcList.add(src);
                        String[] range = rangeStr.split(CommonConstants.SYMBOL_COLON);
                        String minStr = range[0];
                        String minOperator = (String) ConvertorUtil.doConvert(ConvertorKey.RANGE_OPERATOR_CONVERTOR,
                            minStr.substring(0, 1));
                        String minValue = minStr.substring(1);
                        SearchCriteria minCriteria = new SearchCriteria();
                        src.setMin(minCriteria);
                        minCriteria.setCriteriaKey(key);
                        minCriteria.setOperator(minOperator);
                        minCriteria.setCriteriaValue(minValue);
                        String maxStr = range[1];
                        String maxOperator = (String) ConvertorUtil.doConvert(ConvertorKey.RANGE_OPERATOR_CONVERTOR,
                            maxStr.substring(maxStr.length() - 1));
                        String maxValue = maxStr.substring(0, maxStr.length() - 1);
                        SearchCriteria maxCriteria = new SearchCriteria();
                        src.setMax(maxCriteria);
                        maxCriteria.setCriteriaKey(key);
                        maxCriteria.setOperator(maxOperator);
                        maxCriteria.setCriteriaValue(maxValue);
                    }
                }
            } catch (Exception e) {
                String requestParamStr = (String) map.get(StringUtil.getOriginalName(requestParam));
                LogUtil.error(SearchRangeCriteria.class, "The invalid input param is " + requestParam + "[" + requestParamStr
                    + "].", e);
                throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, "The invalid input param is " + requestParam
                    + "[" + requestParamStr + "].Error info[" + e.getMessage() + "]");
            }
        }
        return criteriaList;
    }

    /**
     * Instantiates a new search range criteria.
     */
    public SearchRangeCriteria() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString()).append("min", this.min)
            .append("max", this.max).toString();
    }

    /**
     * Gets the min.
     * 
     * @return the min
     */
    public SearchCriteria getMin() {
        return this.min;
    }

    /**
     * Sets the min.
     * 
     * @param min
     *            the min to set
     */
    public void setMin(final SearchCriteria min) {
        this.min = min;
    }

    /**
     * Gets the max.
     * 
     * @return the max
     */
    public SearchCriteria getMax() {
        return this.max;
    }

    /**
     * Sets the max.
     * 
     * @param max
     *            the max to set
     */
    public void setMax(final SearchCriteria max) {
        this.max = max;
    }

}
