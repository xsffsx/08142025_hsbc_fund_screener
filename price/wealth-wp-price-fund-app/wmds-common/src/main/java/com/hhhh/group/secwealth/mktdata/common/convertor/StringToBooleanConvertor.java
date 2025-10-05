/*
 */
package com.hhhh.group.secwealth.mktdata.common.convertor;

import java.util.HashMap;
import java.util.Map;

import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;

/**
 * The Class StringToBooleanConvertor.
 */
public class StringToBooleanConvertor implements Convertor<String, Boolean> {

    /** The convert map. */
    private Map<String, Boolean> convertMap = new HashMap<>();

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.hhhh.group.secwealth.mktdata.convertor.Convertor#doConvert(java.lang
     * .Object, java.lang.Object[])
     */
    public Boolean doConvert(final String in, final Object... params) {
        Boolean out = null;
        if (StringUtil.isValid(in)) {
            out = convertMap.get(in);
        }
        return out;
    }

    /**
     * Gets the convert map.
     * 
     * @return the convertMap
     */
    public Map<String, Boolean> getConvertMap() {
        return this.convertMap;
    }

    /**
     * Sets the convert map.
     * 
     * @param convertMap
     *            the convertMap to set
     */
    public void setConvertMap(final Map<String, Boolean> convertMap) {
        this.convertMap = convertMap;
    }

}
