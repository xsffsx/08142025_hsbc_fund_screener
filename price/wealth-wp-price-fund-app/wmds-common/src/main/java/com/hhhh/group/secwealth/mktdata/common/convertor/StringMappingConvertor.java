/*
 */
package com.hhhh.group.secwealth.mktdata.common.convertor;

import java.util.HashMap;
import java.util.Map;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;

/**
 * The Class StringMappingConvertor.
 */
public class StringMappingConvertor implements Convertor<String, String> {

    /** The string map. */
    private Map<String, String> stringMap = new HashMap<>();

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.hhhh.group.secwealth.mktdata.convertor.Convertor#doConvert(java.lang
     * .Object)
     */
    public String doConvert(final String in, final Object... params) {
        String out = null;
        if (StringUtil.isValid(in)) {
            out = this.stringMap.get(in);
            if (null == out) {
                out = this.stringMap.get(CommonConstants.DEFAULT);
            }
        }
        return out;
    }

    /**
     * Sets the string map.
     * 
     * @param stringMap
     *            the stringMap to set
     */
    public void setStringMap(final Map<String, String> stringMap) {
        this.stringMap = stringMap;
    }
}
