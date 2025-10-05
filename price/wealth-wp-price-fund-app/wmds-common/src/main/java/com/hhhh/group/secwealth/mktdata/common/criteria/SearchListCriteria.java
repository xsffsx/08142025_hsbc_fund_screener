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
 * The Class SearchListCriteria.
 */
public class SearchListCriteria {

    /** The criteria key. */
    private String criteriaKey;

    /** The items. */
    private List<SearchListCriteriaItem> items;

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
    public static List<SearchListCriteria> getFromContext(final Map<String, Object> map, final String requestParam,
        final String convertorKey, final boolean isCheckKey) {
        List<SearchListCriteria> criteriaList = null;
        List<String> criteria = (List<String>) map.get(requestParam);
        if (ListUtil.isValid(criteria)) {
            criteriaList = new ArrayList<>();
            try {
                for (String key : criteria) {
                    SearchListCriteria sc = new SearchListCriteria();
                    sc.setCriteriaKey(key);
                    if (null != convertorKey && !CommonConstants.ALL.equals(key)) {
                        String criteriaKey = (String) ConvertorUtil.doConvert(convertorKey, key);
                        if (isCheckKey && null == criteriaKey) {
                            String requestParamStr = (String) map.get(StringUtil.getOriginalName(requestParam));
                            LogUtil.error(SearchMinMaxCriteria.class, "The invalid input param is " + requestParam + "["
                                + requestParamStr + "]");
                            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, "The invalid input param is "
                                + requestParam + "[" + requestParamStr + "]");
                        }
                    }
                    criteriaList.add(sc);
                }
            } catch (Exception e) {
                String requestParamStr = (String) map.get(StringUtil.getOriginalName(requestParam));
                LogUtil.error(SearchMinMaxCriteria.class, "The invalid input param is " + requestParam + "[" + requestParamStr
                    + "].Error info[" + e.getMessage() + "]", e);
                throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, "The invalid input param is " + requestParam
                    + "[" + requestParamStr + "].Error info[" + e.getMessage() + "]");
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
            .append("criteriaKey", this.criteriaKey).append("items", this.items).toString();
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
     * Gets the items.
     * 
     * @return the items
     */
    public List<SearchListCriteriaItem> getItems() {
        return this.items;
    }

    /**
     * Sets the items.
     * 
     * @param items
     *            the items to set
     */
    public void setItems(final List<SearchListCriteriaItem> items) {
        this.items = items;
    }

}
