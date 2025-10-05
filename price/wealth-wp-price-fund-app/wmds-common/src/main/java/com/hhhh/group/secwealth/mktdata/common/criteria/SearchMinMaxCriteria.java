/*
 */
package com.hhhh.group.secwealth.mktdata.common.criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.hhhh.group.secwealth.mktdata.common.convertor.ConvertorUtil;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;

/**
 * The Class SearchMinMaxCriteria.
 */
public class SearchMinMaxCriteria {

    /** The criteria key. */
    private String criteriaKey;

    /** The minimum. */
    private Object minimum;

    /** The maximum. */
    private Object maximum;

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
    public static List<SearchMinMaxCriteria> getFromContext(final Map<String, Object> map, final String requestParam,
        final String convertorKey, final boolean isCheckKey) {
        List<SearchMinMaxCriteria> criteriaList = null;
        List<String> criteria = (List<String>) map.get(requestParam);
        if (null != criteria) {
            if (ListUtil.isValid(criteria)) {
                try {
                    List<SearchMinMaxCriteria> minMaxCriteriaList = new ArrayList<SearchMinMaxCriteria>();
                    for (String c : criteria) {
                        SearchMinMaxCriteria sc = new SearchMinMaxCriteria();
                        sc.setCriteriaKey(c);
                        if (null != convertorKey && !CommonConstants.ALL.equals(c)) {
                            String criteriaKey = (String) ConvertorUtil.doConvert(convertorKey, c);
                            if (isCheckKey && null == criteriaKey) {
                                String requestParamStr = (String) map.get(StringUtil.getOriginalName(requestParam));
                                LogUtil.error(SearchMinMaxCriteria.class, "The invalid input param is " + requestParam + "["
                                    + requestParamStr + "]");
                                throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, "The invalid input param is "
                                    + requestParam + "[" + requestParamStr + "].");
                            }
                        }
                        minMaxCriteriaList.add(sc);
                    }
                    criteriaList = minMaxCriteriaList;
                } catch (Exception e) {
                    String requestParamStr = (String) map.get(StringUtil.getOriginalName(requestParam));
                    LogUtil.error(SearchMinMaxCriteria.class, "The invalid input param is " + requestParam + "[" + requestParamStr
                        + "].Error info[" + e.getMessage() + "]", e);
                    throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, "The invalid input param is "
                        + requestParam + "[" + requestParamStr + "].Error info[" + e.getMessage() + "]");
                }
            }
        }
        return criteriaList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString())
            .append("criteriaKey", this.criteriaKey).append("minimum", this.minimum).append("maximum", this.maximum).toString();
    }

    /**
     * Gets the criteria key.
     * 
     * @return the criteriaKey
     */
    public String getCriteriaKey() {
        return this.criteriaKey;
    }

    /**
     * Sets the criteria key.
     * 
     * @param criteriaKey
     *            the criteriaKey to set
     */
    public void setCriteriaKey(final String criteriaKey) {
        this.criteriaKey = criteriaKey;
    }

    /**
     * Gets the minimum.
     * 
     * @return the minimum
     */
    public Object getMinimum() {
        return this.minimum;
    }

    /**
     * Sets the minimum.
     * 
     * @param minimum
     *            the minimum to set
     */
    public void setMinimum(final Object minimum) {
        this.minimum = minimum;
    }

    /**
     * Gets the maximum.
     * 
     * @return the maximum
     */
    public Object getMaximum() {
        return this.maximum;
    }

    /**
     * Sets the maximum.
     * 
     * @param maximum
     *            the maximum to set
     */
    public void setMaximum(final Object maximum) {
        this.maximum = maximum;
    }

}
