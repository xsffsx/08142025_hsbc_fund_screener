/*
 */
package com.hhhh.group.secwealth.mktdata.common.convertor;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class StringToBooleanConvertor.
 */
public class BooleanToStringConvertor implements Convertor<Boolean, String> {

    /** The convert map. */
    private Map<Boolean, String> convertMap = new HashMap<>();

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.hhhh.group.secwealth.mktdata.convertor.Convertor#doConvert(java.lang
     * .Object, java.lang.Object[])
     */
    public String doConvert(final Boolean in, final Object... params) {
        String out = null;
        if (null != in) {
            out = this.convertMap.get(in);
        }
        return out;
    }

    /**
     * Gets the convert map.
     * 
     * @return the convertMap
     */
    public Map<Boolean, String> getConvertMap() {
        return this.convertMap;
    }

    /**
     * Sets the convert map.
     * 
     * @param convertMap
     *            the convertMap to set
     */
    public void setConvertMap(final Map<Boolean, String> convertMap) {
        this.convertMap = convertMap;
    }

}
