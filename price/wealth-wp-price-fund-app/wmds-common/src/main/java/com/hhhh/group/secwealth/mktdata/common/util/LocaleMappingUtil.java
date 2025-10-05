/*
 */
package com.hhhh.group.secwealth.mktdata.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;


@Component("localeMappingUtil")
public  class LocaleMappingUtil {

    private Map<String, String> localeMapping;

    @Value("#{systemConfig['localeMapping']}")
    private String[] locales;

    @PostConstruct
    public void init() throws Exception {
        this.localeMapping = new ConcurrentHashMap<String, String>();
        if (null != this.locales && this.locales.length > 0) {
            for (String s : this.locales) {
                String[] mappings = s.split(CommonConstants.SYMBOL_COLON);
                this.localeMapping.put(mappings[0], mappings[1]);
            }
        }
    }

    // key == countryCode+.+locale()
    public int getNameByIndex(final String key) throws Exception {
        String localeMapping = this.localeMapping.get(key);
        if (StringUtil.isValid(localeMapping)) {
            return Integer.parseInt(localeMapping);
        } else {
            return 0;
        }
    }
}
